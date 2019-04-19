/**
 * DatesServiceSoapType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.daehosting.webservices.dateservice;

public interface DatesServiceSoapType extends java.rmi.Remote {

    /**
     * Returns an array of main language IDs
     */
    public java.lang.String[] mainLanguages() throws java.rmi.RemoteException;

    /**
     * Returns an array of sub language IDs
     */
    public java.lang.String[] subLanguages() throws java.rmi.RemoteException;

    /**
     * Returns the Default Language ID for the system
     */
    public int systemDefaultLanguage() throws java.rmi.RemoteException;

    /**
     * Returns the numeric representation for the passed Main and
     * Sub Language. Passing empty strings results in a NEUTRAL (sub)language.
     */
    public int languageId(java.lang.String sMainLanguage, java.lang.String sSubLanguage) throws java.rmi.RemoteException;

    /**
     * Returns the dayname for the given language and Daynumber
     */
    public java.lang.String dayName(int iLanguage, int iDay, boolean bAbbreviated) throws java.rmi.RemoteException;

    /**
     * Returns an array with all day names of the week for the given
     * language
     */
    public java.lang.String[] dayNames(int iLanguage, boolean bAbbreviated) throws java.rmi.RemoteException;

    /**
     * Returns the monthname for the given language and Monthnumber
     */
    public java.lang.String monthName(int iLanguage, int iMonth, boolean bAbbreviated) throws java.rmi.RemoteException;

    /**
     * Returns an array with all month names of the week for the given
     * language
     */
    public java.lang.String[] monthNames(int iLanguage, boolean bAbbreviated, boolean bUse13Months) throws java.rmi.RemoteException;
}
