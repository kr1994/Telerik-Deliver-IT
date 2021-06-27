package com.telerikacademy.web.deliver_it.utils;

public abstract class Queries {
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String EMAIL = "email";
    public static final String STREET_LINE_1 = "str1";
    public static final String STREET_LINE_2 = "str2";
    public static final String POST_CODE = "postCode";
    public static final String CITY_NAME = "cityName";
    public static final String COUNTRY_NAME = "countryName";
    public static final String CITY_ID = "cityId";
    public static final String COUNTRY_ID = "countryId";

    public static final String ADDRESS_BY_NAME_MATCHES =
            " a.street1 = :" + STREET_LINE_1 +
                    " and a.street2 = :" + STREET_LINE_2 +
                    " and a.postCode = :" + POST_CODE +
                    " and a.city.cityName = :" + CITY_NAME +
                    " and a.city.country.countryName = :" + COUNTRY_NAME;

    public static final String USER_BY_NAME_MATCHES =
            " u.firstName = :" + FIRST_NAME +
                    " and u.lastName = :" + LAST_NAME +
                    " and u.email = :" + EMAIL;

    public static final String ALL_USER_LIKE_AND =
            " cu.firstName like concat('%', :" + FIRST_NAME + " ,'%') " +
                    " and cu.lastName like concat('%', :" + LAST_NAME + " ,'%') " +
                    " and cu.email like concat('%',:" + EMAIL + ",'%') ";

    public static final String COUNTRY_INFO_MATCHES =
            " a.city.country.countryId = :" + COUNTRY_ID +
                    " and a.city.country.countryName = :" + COUNTRY_NAME;

    public static final String CITY_INFO_MATCHES =
            " a.city.cityId = :" + CITY_ID +
                    " and a.city.cityName = :" +CITY_NAME;

    public static final String ALL_USER_LIKE_OR =
            " cu.firstName like concat('%', :" + FIRST_NAME + " ,'%') " +
                    " or cu.lastName like concat('%', :" + LAST_NAME + " ,'%') " +
                    " or cu.email like concat('%', :" + EMAIL + " ,'%') ";


}
