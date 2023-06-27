package com.aurora.gplayapi.data.builders

import com.aurora.gplayapi.data.models.App
import com.aurora.gplayapi.data.models.Artwork
import com.aurora.gplayapi.utils.dig

object WebAppBuilder {

    fun build(data: Any): App {
        val app = App(data.dig(0, 0))
        app.displayName = data.dig(3)
        app.description = data.dig(13, 1)
        app.developerName = data.dig(14)
        app.downloadString = data.dig(15)
        app.categoryName = data.dig(5)
        app.iconArtwork = Artwork().apply {
            url = data.dig(1, 3, 2)
            type = 1
        }
        app.coverArtwork = Artwork().apply {
            url = data.dig(22, 3, 2)
        }
        app.labeledRating = data.dig(4, 0)
        app.isFree = data.dig<String>(25, 0, 0, 0, 0, 1, 0, 2).isEmpty() == true

        return app
    }
}
