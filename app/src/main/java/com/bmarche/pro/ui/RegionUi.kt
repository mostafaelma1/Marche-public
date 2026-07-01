package com.bmarche.pro.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Anchor
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.BeachAccess
import androidx.compose.material.icons.filled.Forest
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.material.icons.filled.Water
import androidx.compose.material.icons.filled.Waves
import androidx.compose.ui.graphics.vector.ImageVector
import com.bmarche.pro.data.model.Region

/**
 * Emblème (icône) distinctif de chaque région, évoquant son identité (port, montagnes,
 * littoral, patrimoine…). Original — pas de reproduction des logos officiels.
 */
fun Region.emblem(): ImageVector = when (this) {
    Region.TANGER_TETOUAN -> Icons.Filled.Anchor        // port du détroit
    Region.ORIENTAL -> Icons.Filled.Terrain             // hauts plateaux
    Region.FES_MEKNES -> Icons.Filled.AccountBalance    // patrimoine impérial
    Region.RABAT_KENITRA -> Icons.Filled.LocationCity   // capitale
    Region.BENI_MELLAL -> Icons.Filled.Agriculture      // plaines agricoles
    Region.CASA_SETTAT -> Icons.Filled.Apartment        // capitale économique
    Region.MARRAKECH_SAFI -> Icons.Filled.Park           // jardins / palmeraie
    Region.DRAA_TAFILALET -> Icons.Filled.Landscape     // désert et vallées
    Region.SOUSS_MASSA -> Icons.Filled.BeachAccess      // littoral d'Agadir
    Region.GUELMIM -> Icons.Filled.Forest               // oasis, porte du désert
    Region.LAAYOUNE -> Icons.Filled.Waves               // côte atlantique
    Region.DAKHLA -> Icons.Filled.Water                 // lagune
}
