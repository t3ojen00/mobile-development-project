package com.example.mobile_development_project.navigation

// NavRoutes is centralized object holding all route names
// and makes it easy to change routes in one place if needed
object NavRoutes {
    // Authentication
    const val Login = "login"
    const val Register = "register"

    // Map & locations
    const val Map = "map"
    const val AddLocation = "add-location"
    const val EditLocation = "edit-location/{id}"  // requires id-parameter
    const val LocationDetail = "location/{id}"     // requires id-parameter

    // User & profile
    const val UserProfile = "user/{id}" // requires id-parameter
    const val Favorites = "favorites"

    // Admin
    const val Admin = "admin"

    // Search & filter
    const val Search = "search"
}