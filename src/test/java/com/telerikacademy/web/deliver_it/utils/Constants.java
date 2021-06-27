package com.telerikacademy.web.deliver_it.utils;

import com.telerikacademy.web.deliver_it.models.*;
import com.telerikacademy.web.deliver_it.models.dto.FilterByWeightDto;
import com.telerikacademy.web.deliver_it.models.dto.ParcelSortingDto;
import com.telerikacademy.web.deliver_it.models.dto.out.ParcelDtoOut;
import org.springframework.http.HttpHeaders;

import java.util.ArrayList;
import java.util.List;

import static com.telerikacademy.web.deliver_it.utils.MockBuilders.*;

public abstract class Constants {

    public static final int MOCK_CITY_ID = 1;
    public static final String CITY_NAME = "Santa Ana";
    public static final int MOCK_COUNTRY_ID = 1;
    public static final String COUNTRY_NAME = "Santa Barbara";

    public static final int MOCK_CUSTOMER_ID = 3;
    public static final String CUSTOMER_FIRST_NAME = "Angel";
    public static final String CUSTOMER_LAST_NAME = "Arhangelov";
    public static final String CUSTOMER_EMAIL = "aa@abv.bg";

    public static final int MOCK_EMPLOYEE_ID = 2;
    public static final String EMPLOYEE_FIRST_NAME = "Yanko";
    public static final String EMPLOYEE_LAST_NAME = "Yunashki";
    public static final String EMPLOYEE_EMAIL = "yayu@abv.bg";
    public static final String EMPLOYEE_PASS_HASH = "-1422502880";
    public static final String EMPLOYEE_PASS = "test";
    public static final double EMPLOYEE_SALT = 0;

    public static final int MOCK_ADDRESS_ID = 7;
    public static final String STR_1 = "Krali Marko";
    public static final String STR_2 = "No. 33";
    public static final String POST = "BG1234";

    public static final int MOCK_WAREHOUSE_ID = 2;
    public static final int MOCK_WH_ADDRESS_ID = 12;
    public static final String WH_STR_1 = "Petko Voivoda";
    public static final String WH_STR_2 = "No. 18";
    public static final String WH_POST = "BG1718";
    public static final String WH_CITY_NAME = "Stara Zagora";
    public static final int WH_CITY_ID = 2;
    public static final String WH_COUNTRY = "MORENA";
    public static final int WH_COUNTRY_ID = 2;

    public static final int MOCK_PARCEL_ID = 2;
    public static final int MOCK_PARCEL_CATEGORY_ID = 1;
    public static final String MOCK_PARCEL_CATEGORY = "Food";
    public static final double MOCK_PARCEL_WEIGHT = 123.45;
    public static final double MOCK_PARCEL_WEIGHT_FACTOR = 0.001;
    public static final String MOCK_UNITS = "gram";
    public static final int MOCK_UNITS_ID = 1;
    public static final double MOCK_UNITS_FACTOR = 0.001;

    public static final String MOCK_SHIPMENT_DEPARTURE_DATE = "2020-01-10";
    public static final String MOCK_SHIPMENT_ARRIVAL_DATE = "2020-01-20";
    public static final String MOCK_SHIPMENT_STATUS = "done";
    public static final int MOCK_SHIPMENT_STATUS_ID = 5;
    public static final int MOCK_SHIPMENT_ID = 4;

    public static final SecurityCredentials SC_EMPLOYEE = mockSecurityCredentialEmployee();
    public static final SecurityCredentials SC_CUSTOMER = mockSecurityCredentialCustomer();
    public static final SecurityCredentials SC_WRONG = mockSecurityCredentialsWrong();
    public static final Address MOCK_ADDRESS = mockAddress();
    public static final City MOCK_CITY = MOCK_ADDRESS.getCity();
    public static final Country MOCK_COUNTRY = MOCK_CITY.getCountry();
    public static final Customer MOCK_CUSTOMER = mockCustomer();
    public static final Employee MOCK_EMPLOYEE = mockEmployeeFromRepo();
    public static final HttpHeaders MOCK_CUSTOMER_HEADERS = mockCustomerHeaders();
    public static final HttpHeaders MOCK_EMPLOYEE_HEADERS = mockEmployeeHeaders();
    public static final Parcel MOCK_PARCEL = mockParcel();
    public static final ParcelCategory MOCK_CATEGORY = mockCategory();
    public static final Shipment MOCK_SHIPMENT = mockShipment();
    public static final ShipmentStatus MOCK_SH_STATUS = mockStatus();
    public static final WeightUnit MOCK_WEIGHT_UNIT = mockWeightUnits();
    public static final FilterByWeightDto MOCK_WEIGHT_FILTER_DTO = mockFilterByWeightDto();
    public static final double MOCK_WEIGHT_FILTER_DTO_MIN_WEIGHT = 2.0;
    public static final double MOCK_WEIGHT_FILTER_DTO_MAX_WEIGHT = 3.0;
    public static final double MOCK_WEIGHT_FILTER_DTO_FACTOR = 0.001;

    public static final Warehouse MOCK_WH = mockWarehouse();
    public static final Address MOCK_WH_ADDRESS = MOCK_WH.getAddress();
    public static final City MOCK_WH_CITY = MOCK_WH_ADDRESS.getCity();
    public static final Country MOCK_WH_COUNTRY = MOCK_WH_CITY.getCountry();
    public static final ParcelSortingDto NEW_PARCEL_SORTING_DTO = new ParcelSortingDto();
    public static final FilterByWeightDto NEW_FILTER_BY_WEIGHT_DTO = new FilterByWeightDto();
    public static final List<Shipment> NEW_SHIPMENT_LIST = new ArrayList<>();
    public static final List<ParcelDtoOut> NEW_PARCEL_DTO_OUT_LIST = new ArrayList<>();

    //Filter & Sort
    public static final double WEIGHT_1 = 1.1;
    public static final double WEIGHT_2 = 2.2;
    public static final double WEIGHT_3 = 3.3;
    public static final double MIN_WEIGHT = 2.0;
    public static final double MAX_WEIGHT = 3.0;
    public static final List<Parcel> MOCK_PARCEL_LIST = List.of(mockParcel());
    public static final List<Shipment> SHIPMENTS_FROM_REPO = mockShipmentsFromRepoWithWeight(WEIGHT_1, WEIGHT_2, WEIGHT_3);
    public static final List<ParcelDtoOut> PARCEL_DTO_OUTS = mockDtoFromMapperWithWeight(WEIGHT_1, WEIGHT_2, WEIGHT_3);
    public static final String DATE_1 = "2020-01-01";
    public static final String DATE_2 = "2021-01-01";
    public static final String DATE_3 = "2022-01-01";
    public static final List<Shipment> UNSORTED_BY_WEIGHT_SHIPMENTS = mockShipmentsFromRepoWithArrivalDate(DATE_2, DATE_1, DATE_3);
    public static final List<ParcelDtoOut> UNSORTED_BY_WEIGHT_PARCELS_DTO_OUT = mockDtoFromMapperWithArrivalDate(DATE_2, DATE_1, DATE_3);
    public static final String WRONG = "wrong";

}
