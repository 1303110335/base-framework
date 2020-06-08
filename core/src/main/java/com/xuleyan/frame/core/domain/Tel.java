/**
 * fshows.com
 * Copyright (C) 2013-2020 All Rights Reserved.
 */
package com.xuleyan.frame.core.domain;

import java.util.Objects;

/**
 * @author xuleyan
 * @version Tel.java, v 0.1 2020-06-07 9:31 PM xuleyan
 */
public class Tel {

    private String areaCode;

    private String phone;

    public Tel(String areaCode, String phone) {
        this.areaCode = areaCode;
        this.phone = phone;
    }

    /**
     * Getter method for property <tt>areaCode</tt>.
     *
     * @return property value of areaCode
     */
    public String getAreaCode() {
        return areaCode;
    }

    /**
     * Setter method for property <tt>areaCode</tt>.
     *
     * @param areaCode value to be assigned to property areaCode
     */
    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    /**
     * Getter method for property <tt>phone</tt>.
     *
     * @return property value of phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Setter method for property <tt>phone</tt>.
     *
     * @param phone value to be assigned to property phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.areaCode, this.phone);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof Tel) {
            Tel t = (Tel)obj;
            return Objects.equals(t.areaCode, this.areaCode) && Objects.equals(t.phone, this.phone);
        }
        return false;
    }

    @Override
    public String toString() {
        return this.areaCode + "-" + this.phone;
    }

}