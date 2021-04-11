package com.example.weather.FromJson;

import com.google.gson.annotations.SerializedName;

public class Rain {

    @SerializedName("1h")
    private String oneH;

    public String getOneH ()
    {
        return oneH;
    }

    public void setOneH (String oneH)
    {
        this.oneH = oneH;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [oneH = "+oneH+"]";
    }
}
