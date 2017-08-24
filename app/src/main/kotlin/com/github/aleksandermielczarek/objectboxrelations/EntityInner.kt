package com.github.aleksandermielczarek.objectboxrelations

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

/**
 * Created by Aleksander Mielczarek on 24.08.2017.
 */
@Entity
data class EntityInner(
        @Id var id: Long = 0,
        var data1: String = "",
        var data2: String = "",
        var data3: String = "",
        var data4: String = "",
        var data5: String = ""
)