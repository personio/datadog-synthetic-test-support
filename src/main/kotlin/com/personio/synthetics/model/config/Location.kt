package com.personio.synthetics.model.config

enum class Location(val value: String) {
    CANADA_CENTRAL_AWS("aws:ca-central-1"),
    N_CALIFORNIA_AWS("aws:us-west-1"),
    N_VIRGINIA_AWS("aws:us-east-1"),
    OHIO_AWS("aws:us-east-2"),
    OREGON_AWS("aws:us-west-2"),
    SAN_PAOLO_AWS("aws:sa-east-1"),
    VIRGINIA_AZURE("azure:eastus"),

    HONG_KONG_AWS("aws:ap-east-1"),
    MUMBAI_AWS("aws:ap-south-1"),
    SEOUL_AWS("aws:ap-northeast-2"),
    SINGAPORE_AWS("aws:ap-southeast-1"),
    SYDNEY_AWS("aws:ap-southeast-2"),
    TOKYO_AWS("aws:ap-northeast-1"),

    CAPE_TOWN_AWS("aws:af-south-1"),
    FRANKFURT_AWS("aws:eu-central-1"),
    IRELAND_AWS("aws:eu-west-1"),
    LONDON_AWS("aws:eu-west-2"),
    PARIS_AWS("aws:eu-west-3"),
    STOCKHOLM_AWS("aws:eu-north-1"),
}
