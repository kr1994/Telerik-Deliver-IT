package com.telerikacademy.web.deliver_it.controllers.mvc;

import com.telerikacademy.web.deliver_it.models.*;
import com.telerikacademy.web.deliver_it.models.dto.FilterByStatusDto;
import com.telerikacademy.web.deliver_it.models.dto.ParcelDto;
import com.telerikacademy.web.deliver_it.models.dto.ParcelSortingDto;
import com.telerikacademy.web.deliver_it.models.dto.FilterByWeightDto;
import com.telerikacademy.web.deliver_it.models.dto.out.ParcelDtoOut;
import com.telerikacademy.web.deliver_it.services.contracts.*;
import com.telerikacademy.web.deliver_it.services.helpers.contracts.sub.ParcelMapper;
import com.telerikacademy.web.deliver_it.services.security.contracts.Security;
import com.telerikacademy.web.deliver_it.utils.exceptions.EntityNotFoundException;
import com.telerikacademy.web.deliver_it.utils.exceptions.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.telerikacademy.web.deliver_it.controllers.mvc.Constants.*;
import static com.telerikacademy.web.deliver_it.controllers.mvc.MvcHelpers.*;
import static com.telerikacademy.web.deliver_it.utils.Builders.*;
import static com.telerikacademy.web.deliver_it.utils.Formatters.isValidWeight;

@Controller
@RequestMapping(PARCELS_URL)
public class ParcelMvcController {

    private final ParcelCategoryService categories;
    private final ShipmentStatusService statuses;
    private final WeightUnitService weightUnits;
    private final WarehouseService warehouses;
    private final ParcelService service;
    private final ParcelMapper mapper;
    private final Security security;

    @Autowired
    public ParcelMvcController(ParcelService service, ParcelMapper mapper, WarehouseService warehouses,
                               ParcelCategoryService categories, ShipmentStatusService statuses,
                               WeightUnitService weightUnits, Security security) {
        this.weightUnits = weightUnits;
        this.categories = categories;
        this.warehouses = warehouses;
        this.statuses = statuses;
        this.security = security;
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping(SORTED_URL)
    public String showSortingPage(Model model, HttpSession session) {
        try {
            setModelForSortingPage(model, session);
            return PARCELS_SORTED_VIEW;
        } catch (UnauthorizedException e) {
            return ACCESS_DENIED;
        }
    }

    @PostMapping(SORTED_URL)
    public String sort(@Valid @ModelAttribute(PARCEL_SORTING_DTO_ATTRIBUTE) ParcelSortingDto dto,
                       BindingResult errors, HttpSession session, Model model) {
        if (errors.hasErrors()) {
            setModelForSortingPage(model, session);
            return PARCELS_SORTED_VIEW;
        }
        try {
            List<ParcelDtoOut> result = getSortedResult(dto, session);
            model.addAttribute(PARCELS_DTO_OUT_SORTING_RESULT_ATTRIBUTE, result);
            return SORTED_RESULT_VIEW;
        } catch (EntityNotFoundException e) {
            model.addAttribute(ERROR_ATTRIBUTE, e.getMessage());
            return NOT_FOUND_VIEW;
        } catch (UnauthorizedException e) {
            return ACCESS_DENIED;
        }
    }


    @GetMapping(FILTER_BY_STATUS_URL)
    public String getFilterByStatusViewPage(HttpSession session,
                                            Model model) {
        try {
            setModelForFilterByStatusView(session, model);
            return FILTER_BY_STATUS_VIEW;
        } catch (UnauthorizedException e) {
            return ACCESS_DENIED;
        }
    }

    @PostMapping(FILTER_BY_STATUS_URL)
    public String filterByStatusOfShipment(@ModelAttribute(FILTER_BY_STATUS_ATTRIBUTE) FilterByStatusDto dto,
                                           HttpSession session, Model model) {
        try {
            setModelForFilterByStatus(dto.getId(), session, model);
            return SORTED_RESULT_VIEW;
        } catch (EntityNotFoundException e) {
            model.addAttribute(ERROR_ATTRIBUTE, e.getMessage());
            return NOT_FOUND_VIEW;
        } catch (UnauthorizedException e) {
            return ACCESS_DENIED;
        }
    }

    @GetMapping(PARCEL_ADDITIONAL_INFO_URL)//customers only
    public String getAdditionalInfo(@PathVariable int id, Model model, HttpSession session) {
        try {
            setModelForAdditionalInfo(model, id, session);
            return SORTED_RESULT_VIEW;
        } catch (EntityNotFoundException e) {
            model.addAttribute(ERROR_ATTRIBUTE, e.getMessage());
            return NOT_FOUND_VIEW;
        } catch (UnauthorizedException e) {
            return ACCESS_DENIED;
        }
    }

    @GetMapping(FILTER_WEIGHT_URL)
    public String showFilterPage(Model model, HttpSession session) {
        try {
            setModelForWeightFilterViewPage(model, session);
            return PARCELS_FILTERED_BY_WEIGHT_VIEW;
        } catch (EntityNotFoundException e) {
            model.addAttribute(ERROR_ATTRIBUTE, e.getMessage());
            return NOT_FOUND_VIEW;
        } catch (UnauthorizedException e) {
            return ACCESS_DENIED;
        }
    }

    @PostMapping(FILTER_WEIGHT_URL)
    public String filterByWeight(@ModelAttribute(WEIGHT_FILTER_DTO_ATTRIBUTE) FilterByWeightDto dto,
                                 HttpSession session, Model model, BindingResult errors) {
        if (hasMinMaxMismatch(dto)) {
            setWeightOptions(dto);
            setErrorsForWeightFilter(errors);
            return PARCELS_FILTERED_BY_WEIGHT_VIEW;
        }
        try {
            setModelForFilterByWeight(dto, session, model);
            return SORTED_RESULT_VIEW;
        } catch (EntityNotFoundException e) {
            model.addAttribute(ERROR_ATTRIBUTE, e.getMessage());
            return NOT_FOUND_VIEW;
        } catch (UnauthorizedException e) {
            return ACCESS_DENIED;
        }
    }

    @GetMapping(GET_BY_ID_URL)
    public String getById(@PathVariable int id, HttpSession session, Model model) {
        try {
            setModelForGetById(model, id, session);
            return PARCEL_VIEW;
        } catch (EntityNotFoundException e) {
            model.addAttribute(ERROR_ATTRIBUTE, e.getMessage());
            return NOT_FOUND_VIEW;
        } catch (UnauthorizedException e) {
            return ACCESS_DENIED;
        }
    }

    @GetMapping(CREATE_URL)
    public String showCreationPage(Model model, HttpSession session) {
        try {
            validateEmployee(session, security);
            model.addAttribute(PARCEL_ATTRIBUTE, new ParcelDto());
            return PARCEL_CREATE_VIEW;
        } catch (EntityNotFoundException e) {
            model.addAttribute(ERROR_ATTRIBUTE, e.getMessage());
            return NOT_FOUND_VIEW;
        } catch (UnauthorizedException e) {
            return ACCESS_DENIED;
        }
    }

    @PostMapping(CREATE_URL)
    public String create(@Valid @ModelAttribute(PARCEL_ATTRIBUTE) ParcelDto dto,
                         BindingResult errors, HttpSession session, Model model) {
        if (errors.hasErrors())
            return PARCEL_CREATE_VIEW;
        try {
            Parcel result = create(dto, session);
            return viewById(result);
        } catch (EntityNotFoundException e) {
            model.addAttribute(ERROR_ATTRIBUTE, e.getMessage());
            return NOT_FOUND_VIEW;
        } catch (UnauthorizedException e) {
            return ACCESS_DENIED;
        }//can have duplicates
    }

    @GetMapping(UPDATE_URL)
    public String showUpdatePage(@PathVariable int id, HttpSession session, Model model) {
        try {
            setParcelDtoTo(model, id, session);
            return PARCEL_UPDATE_VIEW;
        } catch (EntityNotFoundException e) {
            model.addAttribute(ERROR_ATTRIBUTE, e.getMessage());
            return NOT_FOUND_VIEW;
        } catch (UnauthorizedException e) {
            return ACCESS_DENIED;
        }
    }

    @PostMapping(UPDATE_URL)
    public String update(@Valid @ModelAttribute(PARCEL_ATTRIBUTE) ParcelDto dto,
                         @PathVariable int id, HttpSession session,
                         BindingResult errors, Model model) {
        if (errors.hasErrors())
            return PARCEL_UPDATE_VIEW;
        try {
            Parcel result = update(dto, id, session);
            return viewById(result);
        } catch (EntityNotFoundException e) {
            model.addAttribute(ERROR_ATTRIBUTE, e.getMessage());
            return NOT_FOUND_VIEW;
        } catch (UnauthorizedException e) {
            return ACCESS_DENIED;
        }//can have duplicates
    }

    @GetMapping(DELETE_URL)
    public String showDeletePage(@PathVariable int id, HttpSession session, Model model) {
        try {
            delete(id, session, model);
            return PARCEL_DELETE_VIEW;
        } catch (EntityNotFoundException e) {
            model.addAttribute(ERROR_ATTRIBUTE, e.getMessage());
            return NOT_FOUND_VIEW;
        } catch (UnauthorizedException e) {
            return ACCESS_DENIED;
        }
    }

    @ModelAttribute(WAREHOUSES_ATTRIBUTE)
    public List<Warehouse> populateWarehouses() {
        return warehouses.getAll();
    }

    @ModelAttribute(PARCEL_CATEGORIES_ATTRIBUTE)
    public List<ParcelCategory> populateCategories() {
        return categories.getAll();
    }

    @ModelAttribute(WEIGHT_UNITS_ATTRIBUTE)
    public List<WeightUnit> populateWeightUnits() {
        return weightUnits.getAll();
    }

    private List<ParcelDtoOut> getSortedResult(ParcelSortingDto dto, HttpSession session) {
        SecurityCredentials sc = getCredential(session);
        dto.setArrivalDateSort(getSortingType(dto.getArrivalDateSort()));
        dto.setWeightSort(getSortingType(dto.getWeightSort()));
        return service.getAllSorted(dto, sc);

    }

    private String viewById(Parcel result) {
        return String.format("redirect:/parcels/%d", result.getId());
    }

    private Parcel create(ParcelDto dto, HttpSession session) {
        SecurityCredentials sc = getCredential(session);
        Parcel input = mapper.fromDto(dto);
        return service.create(input, sc);
    }

    private Parcel update(ParcelDto dto, int parcelId, HttpSession session) {
        SecurityCredentials sc = getCredential(session);
        Parcel input = mapper.fromDto(parcelId, dto);
        return service.update(input, sc);
    }

    private void delete(int id, HttpSession session, Model model) {
        SecurityCredentials sc = getCredential(session);
        Parcel deleted = service.delete(id, sc);
        model.addAttribute(PARCEL_ATTRIBUTE, deleted);
    }

    private void setParcelDtoTo(Model model, int id, HttpSession session) {
        SecurityCredentials sc = getValidatedEmployeeCredential(session, security);
        Parcel stock = service.getById(id, sc);
        ParcelDto dto = buildDto(stock);
        model.addAttribute(PARCEL_ATTRIBUTE, dto);
    }

    private void setModelForSortingPage(Model model, HttpSession session) {
        SecurityCredentials sc = getValidatedCredential(session, security);
        if (security.isCustomer(sc))
            model.addAttribute("isCustomer", "yes");
        model.addAttribute(PARCEL_SORTING_DTO_ATTRIBUTE, new ParcelSortingDto());
        model.addAttribute(SORTING_TYPES_ATTRIBUTE, SORTING_TYPES_NAMES);
        setWarehousesWhitEmptyOptionTo(model);
        setCategoriesWhitEmptyOptionTo(model);
    }

    private void setWarehousesWhitEmptyOptionTo(Model model) {
        Warehouse empty = buildEmptyWarehouse();
        empty.getAddress().getCity().setCityName(SORTING_TYPE_ANY);
        empty.getAddress().getCity().getCountry().setCountryName(SORTING_TYPE_ANY);
        List<Warehouse> whitEmptyOption = new ArrayList<>(List.of(empty));
        whitEmptyOption.addAll(warehouses.getAll());
        model.addAttribute(WAREHOUSES_ATTRIBUTE, whitEmptyOption);
    }

    private void setCategoriesWhitEmptyOptionTo(Model model) {
        ParcelCategory empty = new ParcelCategory();
        empty.setParcelCategory(SORTING_TYPE_ANY);
        List<ParcelCategory> whitEmptyOption = new ArrayList<>(List.of(empty));
        whitEmptyOption.addAll(categories.getAll());
        model.addAttribute(PARCEL_CATEGORIES_ATTRIBUTE, whitEmptyOption);
    }

    private void setModelForFilterByStatusView(HttpSession session, Model model) {
        SecurityCredentials sc = getValidatedCredential(session, security);
        FilterByStatusDto dto = new FilterByStatusDto();
        dto.setStatuses(statuses.getAll(sc));
        model.addAttribute(FILTER_BY_STATUS_ATTRIBUTE, dto);
    }

    private void setModelForAdditionalInfo(Model model, int parcelId, HttpSession session) {
        SecurityCredentials sc = getValidatedCredential(session, security);
        ParcelDtoOut result = service.getStatusOfParcel(parcelId, sc);
        model.addAttribute(PARCELS_DTO_OUT_SORTING_RESULT_ATTRIBUTE, List.of(result));
    }

    private void setModelForGetById(Model model, int parcelId, HttpSession session) {
        SecurityCredentials sc = getValidatedCredential(session, security);
        Parcel parcel = service.getById(parcelId, sc);
        model.addAttribute(PARCEL_ATTRIBUTE, parcel);
    }

    private void setModelForFilterByWeight(FilterByWeightDto dto, HttpSession session, Model model) {
        SecurityCredentials sc = getCredential(session);
        List<ParcelDtoOut> result = service.filterByWeight(dto, sc);
        model.addAttribute(PARCELS_DTO_OUT_SORTING_RESULT_ATTRIBUTE, result);
    }

    private void setModelForFilterByStatus(int statusId, HttpSession session, Model model) {
        SecurityCredentials sc = getCredential(session);
        List<ParcelDtoOut> result = service.filterByStatusFromShipment(statusId, sc);
        model.addAttribute(PARCELS_DTO_OUT_SORTING_RESULT_ATTRIBUTE, result);
    }

    private void setModelForWeightFilterViewPage(Model model, HttpSession session) {
        getValidatedCredential(session, security);
        FilterByWeightDto dto = new FilterByWeightDto();
        setWeightOptions(dto);
        model.addAttribute(WEIGHT_FILTER_DTO_ATTRIBUTE, dto);
    }

    private void setWeightOptions(FilterByWeightDto dto) {
        List<String> options = populateWeightUnits().stream()
                .map(WeightUnit::getUnits)
                .collect(Collectors.toList());
        dto.setOptions(options);
    }

    private void setErrorsForWeightFilter(BindingResult errors) {
        errors.rejectValue("minWeight", "weight.misMatch",
                "Min weight mast be less than max weight");
        errors.rejectValue("maxWeight", "weight.misMatch",
                "Max weight mast be more than min weight");
    }

    private boolean hasMinMaxMismatch(FilterByWeightDto dto) {
        setWeightFactors(dto);
        return isValidWeight(dto.getMaxWeight()) &&
                isValidWeight(dto.getMinWeight()) &&
                dto.getMinWeight() * dto.getMinWeightFactor() >
                        dto.getMaxWeight() * dto.getMaxWeightFactor();
    }

    private void setWeightFactors(FilterByWeightDto dto) {
        double minFactor = getFactor(dto.getMinUnit());
        dto.setMinWeightFactor(minFactor);
        double maxFactor = getFactor(dto.getMaxUnit());
        dto.setMaxWeightFactor(maxFactor);
    }

    private double getFactor(String unit) {
        return populateWeightUnits().stream()
                .filter(u -> u.getUnits().equalsIgnoreCase(unit))
                .map(WeightUnit::getFactor)
                .findFirst()
                .orElse(0.0);
    }

    private String getSortingType(String sortingType) {
        switch (sortingType) {
            case NONE:
                return "";
            case ASCENDING:
                return (SORTING_TYPE_ASCENDING);
            case DESCENDING:
                return (SORTING_TYPE_DESCENDING);
            default:
                throw new EntityNotFoundException("Unsupported sorting type");
        }
    }
}
