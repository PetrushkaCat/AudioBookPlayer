package cat.petrushkacat.audiobookplayer.data.mappers

import cat.petrushkacat.audiobookplayer.data.dto.RootFolderEntityDB
import cat.petrushkacat.audiobookplayer.domain.models.RootFolderEntity

fun RootFolderEntityDB.toRootFolderEntity(): RootFolderEntity {
    return RootFolderEntity(
        uri = this.uri,
        name = this.name,
        isCurrent = this.isCurrent
    )
}

fun RootFolderEntity.toRootFolderEntityDB(): RootFolderEntityDB {
    return RootFolderEntityDB(
        uri = this.uri,
        name = this.name,
        isCurrent = this.isCurrent
    )
}