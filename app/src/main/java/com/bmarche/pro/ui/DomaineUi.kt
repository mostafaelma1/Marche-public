package com.bmarche.pro.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Security
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.bmarche.pro.data.model.Domaine
import com.bmarche.pro.ui.theme.DomaineColors

/** Couleur d'accent d'un domaine (liseré de carte, pastille). */
fun Domaine.couleur(): Color = DomaineColors[ordinal % DomaineColors.size]

/** Icône représentative d'un domaine. */
fun Domaine.icone(): ImageVector = when (this) {
    Domaine.RESTAURATION -> Icons.Filled.Restaurant
    Domaine.NETTOYAGE -> Icons.Filled.CleaningServices
    Domaine.TRAVAUX -> Icons.Filled.Construction
    Domaine.FOURNITURES -> Icons.Filled.Inventory2
    Domaine.TRANSPORT -> Icons.Filled.DirectionsBus
    Domaine.GARDIENNAGE -> Icons.Filled.Security
    Domaine.INFORMATIQUE -> Icons.Filled.Computer
    Domaine.ESPACES_VERTS -> Icons.Filled.Park
}
