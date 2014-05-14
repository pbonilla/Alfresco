/*
 * Copyright (c) 2014, Flatirons Solutions, Inc. All Rights Reserved.
 */

package com.pernixsolutions.webscripts.WebscriptUtil;

import org.springframework.extensions.webscripts.WebScriptRequest;

/**
 * @author fsdev
 *
 */
public class RequestHelper {
    /**
     *
     * @param req
     * @return requesString
     */
    public static String getRequestString(final WebScriptRequest req){
        String request="";
        String[] parametersNames = req.getParameterNames();
        for(String paramName : parametersNames){
            request += "Param "+paramName+" : "+req.getParameterValues(paramName);
        }
        return request;
    }
}
