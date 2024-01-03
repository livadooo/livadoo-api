rootProject.name = "livadoo-api"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")
}

include(
    "app",
    "authority-search-service",
    "authority-search-service-proxy",
    "account-service",
    "auth-service",
    "auth-service-proxy",
    "user-service",
    "user-service-proxy",
    "shared",
    "notification-service",
    "inventory-service",
    "storage-service",
    "customer-service",
    "otp-service",
    "phone-validation-service",
    "phone-validation-service-proxy",
    "otp-service-proxy",
    "notification-service-proxy",
    "storage-service-proxy",
    "user-search-service",
    "user-search-service-proxy",
    "customer-service-proxy",
    "inventory-service-proxy",
    "utils:exception-utils",
    "utils:security-utils",
    "utils:spring-utils",
    "utils:user-utils",
    "role-service",
    "role-service-proxy",
    "permission-service",
    "permission-service-proxy",
)
