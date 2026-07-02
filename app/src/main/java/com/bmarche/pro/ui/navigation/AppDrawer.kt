package com.bmarche.pro.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import com.bmarche.pro.R
import com.bmarche.pro.data.model.TypePublication
import com.bmarche.pro.ui.estArabe
import com.bmarche.pro.ui.label

/**
 * Menu latéral « enterprise » à sections repliables : en-tête de marque, navigation
 * principale, groupes Appels d'offres / Résultats avec sous-entrées, raccourcis outils.
 */
@Composable
fun AppDrawerContent(
    onFermer: () -> Unit,
    onOuvrirOnglet: (TopDestination) -> Unit,
    onOuvrirType: (TypePublication) -> Unit,
    onOuvrirMaSociete: () -> Unit
) {
    ModalDrawerSheet(drawerContainerColor = MaterialTheme.colorScheme.surface) {
        Column(Modifier.verticalScroll(rememberScrollState())) {
            // --- En-tête de marque ---
            Row(
                Modifier.padding(start = 20.dp, end = 8.dp, top = 20.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.logo_bmarche),
                    contentDescription = null,
                    modifier = Modifier.size(42.dp)
                )
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        stringResource(R.string.app_name),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        stringResource(R.string.drawer_sous_titre),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = onFermer) {
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = stringResource(R.string.drawer_fermer),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(Modifier.height(8.dp))

            DrawerItem(Icons.Filled.Dashboard, stringResource(R.string.drawer_tableau_bord)) {
                onOuvrirOnglet(TopDestination.ACCUEIL)
            }

            DrawerSection(
                icone = Icons.Filled.Gavel,
                libelle = stringResource(R.string.drawer_appels_offres),
                sousEntrees = listOf(
                    TypePublication.MARCHE_PUBLIC,
                    TypePublication.BON_COMMANDE,
                    TypePublication.MARCHE_PRIVE,
                    TypePublication.PROGRAMME_PREVISIONNEL
                ),
                ouvertParDefaut = true,
                onOuvrirType = onOuvrirType
            )

            DrawerSection(
                icone = Icons.Filled.EmojiEvents,
                libelle = stringResource(R.string.drawer_resultats),
                sousEntrees = listOf(
                    TypePublication.RESULTAT_DEFINITIF,
                    TypePublication.EXTRAIT_PV,
                    TypePublication.DECISION_RESILIATION
                ),
                ouvertParDefaut = false,
                onOuvrirType = onOuvrirType
            )

            Spacer(Modifier.height(8.dp))
            HorizontalDivider(
                Modifier.padding(horizontal = 20.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )
            Spacer(Modifier.height(8.dp))

            DrawerItem(Icons.Filled.Favorite, TopDestination.FAVORIS.label()) { onOuvrirOnglet(TopDestination.FAVORIS) }
            DrawerItem(Icons.Filled.Business, TopDestination.SOCIETES.label()) { onOuvrirOnglet(TopDestination.SOCIETES) }
            DrawerItem(Icons.Filled.Calculate, stringResource(R.string.prix_titre)) { onOuvrirOnglet(TopDestination.PRIX) }

            Spacer(Modifier.height(8.dp))
            HorizontalDivider(
                Modifier.padding(horizontal = 20.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )
            Spacer(Modifier.height(8.dp))

            DrawerItem(Icons.Filled.Apartment, stringResource(R.string.drawer_ma_societe), onClick = onOuvrirMaSociete)
            DrawerItem(Icons.Filled.Person, stringResource(R.string.drawer_profil)) { onOuvrirOnglet(TopDestination.PROFIL) }

            Spacer(Modifier.height(12.dp))
            HorizontalDivider(
                Modifier.padding(horizontal = 20.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )

            // --- Sélecteur de langue FR / AR ---
            Text(
                stringResource(R.string.drawer_langue),
                modifier = Modifier.padding(start = 24.dp, top = 12.dp, bottom = 6.dp),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                Modifier.padding(horizontal = 24.dp),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(10.dp)
            ) {
                LangueChip("Français", selectionne = !estArabe()) {
                    AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("fr"))
                }
                LangueChip("العربية", selectionne = estArabe()) {
                    AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("ar"))
                }
            }

            Spacer(Modifier.height(20.dp))
        }
    }
}

/** Pastille de choix de langue. */
@Composable
private fun LangueChip(libelle: String, selectionne: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        color = if (selectionne) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.surface,
        border = BorderStroke(
            1.dp,
            if (selectionne) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.outlineVariant
        )
    ) {
        Text(
            libelle,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.labelLarge,
            color = if (selectionne) MaterialTheme.colorScheme.onPrimaryContainer
            else MaterialTheme.colorScheme.onSurface
        )
    }
}

/** Entrée simple du menu (icône + libellé). */
@Composable
private fun DrawerItem(icone: ImageVector, libelle: String, onClick: () -> Unit) {
    Row(
        Modifier
            .padding(horizontal = 12.dp, vertical = 2.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icone,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(22.dp)
        )
        Spacer(Modifier.width(14.dp))
        Text(
            libelle,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

/** Section repliable : rangée d'en-tête avec chevron animé, sous-entrées à liseré. */
@Composable
private fun DrawerSection(
    icone: ImageVector,
    libelle: String,
    sousEntrees: List<TypePublication>,
    ouvertParDefaut: Boolean,
    onOuvrirType: (TypePublication) -> Unit
) {
    var ouvert by rememberSaveable { mutableStateOf(ouvertParDefaut) }
    val rotation by animateFloatAsState(
        targetValue = if (ouvert) 180f else 0f,
        animationSpec = tween(durationMillis = 250),
        label = "chevron"
    )

    Column(Modifier.padding(horizontal = 12.dp, vertical = 2.dp)) {
        Row(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(
                    if (ouvert) MaterialTheme.colorScheme.surfaceContainer
                    else MaterialTheme.colorScheme.surface
                )
                .clickable { ouvert = !ouvert }
                .padding(horizontal = 12.dp, vertical = 13.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icone,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(22.dp)
            )
            Spacer(Modifier.width(14.dp))
            Text(
                libelle,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Icon(
                Icons.Filled.KeyboardArrowDown,
                contentDescription = if (ouvert) "Replier" else "Déplier",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .size(20.dp)
                    .rotate(rotation)
            )
        }

        AnimatedVisibility(
            visible = ouvert,
            enter = expandVertically(tween(250)) + fadeIn(tween(250)),
            exit = shrinkVertically(tween(250)) + fadeOut(tween(200))
        ) {
            Column(Modifier.padding(start = 24.dp, top = 2.dp, bottom = 4.dp)) {
                sousEntrees.forEach { type ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { onOuvrirType(type) }
                            .padding(horizontal = 10.dp, vertical = 11.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            Modifier
                                .width(2.dp)
                                .height(16.dp)
                                .clip(RoundedCornerShape(50))
                                .background(MaterialTheme.colorScheme.outlineVariant)
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            type.label(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}
