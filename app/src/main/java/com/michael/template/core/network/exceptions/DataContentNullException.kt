package com.michael.template.core.network.exceptions

class DataContentNullException(queryName: String, dataPoint: String) :
    NullPointerException("While mapping API query ($queryName) the data content ($dataPoint) has been null")
