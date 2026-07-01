package com.bmarche.pro.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.ui.graphics.vector.ImageVector
import com.bmarche.pro.data.model.TypePublication

/** Icône représentative de chaque catégorie de publication. */
fun TypePublication.icone(): ImageVector = when (this) {
    TypePublication.MARCHE_PUBLIC -> Icons.Filled.Gavel
    TypePublication.BON_COMMANDE -> Icons.Filled.ReceiptLong
    TypePublication.MARCHE_PRIVE -> Icons.Filled.Lock
    TypePublication.PROGRAMME_PREVISIONNEL -> Icons.Filled.CalendarMonth
    TypePublication.RESULTAT_DEFINITIF -> Icons.Filled.EmojiEvents
    TypePublication.EXTRAIT_PV -> Icons.Filled.Description
    TypePublication.DECISION_RESILIATION -> Icons.Filled.Cancel
}
