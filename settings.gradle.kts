rootProject.name = "livadoo-api"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")
}

include(
    "app",
    "jwt-security-lib",
    "user-service",
    "user-service-proxy",
    "common",
    "notification-service",
    "inventory-service",
    "storage-service",
    "customer-service",
    "notification-service-proxy",
    "storage-service-proxy",
    "customer-service-proxy",
    "inventory-service-proxy",
    "utils:user-utils",
    "role-service",
)
