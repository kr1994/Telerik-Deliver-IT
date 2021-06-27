package com.telerikacademy.web.deliver_it.utils.marckedForDelition;

import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {

   /* private final AddressRepository addresses;
    private final Security security;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository, Security security) {
        this.addresses = addressRepository;
        this.security = security;
    }

    // visible to employees and //customers if it is there's
    @Override
    public Address getById(int id, SecurityCredentials securityCredentials) {
        validate(id, securityCredentials);
        return addresses.getById(id);
    }

    //employees
    @Override
    public Address create(Address newAddress, SecurityCredentials sc) {
        if (isEmployee(sc))
            return addresses.create(newAddress);
        throw new UnauthorizedException();
    }

    //employees
    @Override
    public Address update(Address address, SecurityCredentials securityCredentials) {
        if (isEmployee(securityCredentials))
            return addresses.update(address);
        throw new UnauthorizedException();

    }

    //employees
    @Override
    public Address delete(int id, SecurityCredentials securityCredentials) {
        if (isEmployee(securityCredentials))
            return addresses.delete(id);
        throw new UnauthorizedException();
    }

    private void validate(int AddressId, SecurityCredentials SC) {
        try {
            validate(addresses.getById(AddressId), SC);
        } catch (EntityNotFoundException e) {
            throw new UnauthorizedException();
        }
    }

    private void validate(Address address, SecurityCredentials SC) {
        if (isEmployee(SC) || isCustomers_sAddress(address, SC))
            return;
        throw new UnauthorizedException();
    }

    private boolean isCustomers_sAddress(Address address, SecurityCredentials sc) {
        return isCustomer(sc) &&
                address.equals(addresses.getByUserId(sc.getId()));
    }

    private boolean isEmployee(SecurityCredentials SC) {
        return security.isEmployee(SC);
    }

    private boolean isCustomer(SecurityCredentials sc) {
        return security.isCustomer(sc);
    }
*/}
