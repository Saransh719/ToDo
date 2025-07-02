package com.example.todo

interface Destinations {
    val route : String
}

object Home : Destinations{
    override val route = "home"
}

object add : Destinations{
    override val route = "add"
}

object Description : Destinations{
    override val route = "description"
}

object edit : Destinations{
    override val route = "edit"
}