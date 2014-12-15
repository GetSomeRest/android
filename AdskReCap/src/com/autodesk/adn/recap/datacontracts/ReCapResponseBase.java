package com.autodesk.adn.recap.datacontracts;


public class ReCapResponseBase
{
    public String Usage;

    public String Resource;

    public ReCapError Error;

    public Boolean IsOk()
    {
        return (Error == null);
    }
}