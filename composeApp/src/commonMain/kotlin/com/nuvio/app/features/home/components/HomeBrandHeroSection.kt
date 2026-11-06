package com.nuvio.app.features.home.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nuvio.app.core.ui.NuvioTokens
import com.nuvio.app.core.ui.nuvio
import nuvio.composeapp.generated.resources.Res
import nuvio.composeapp.generated.resources.app_brand_name
import nuvio.composeapp.generated.resources.home_brand_headline
import nuvio.composeapp.generated.resources.home_brand_support
import nuvio.composeapp.generated.resources.home_cta_continue
import nuvio.composeapp.generated.resources.home_cta_multiview
import org.jetbrains.compose.resources.stringResource

/**
 * MultiNutz first viewport: brand, one headline, one support line, CTA group.
 * Continue Watching is primary; Multiview is reachable but not dominant.
 */
@Composable
fun HomeBrandHeroSection(
    modifier: Modifier = Modifier,
    sectionPadding: Dp = 16.dp,
    onContinueClick: (() -> Unit)? = null,
    onMultiViewerClick: (() -> Unit)? = null,
) {
    val tokens = MaterialTheme.nuvio
    val enterAlpha = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        enterAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = tokens.motion.slowMillis,
                easing = tokens.motion.decelerate,
            ),
        )
    }

    val steelWash = Brush.linearGradient(
        colors = listOf(
            tokens.colors.accent.copy(alpha = 0.22f),
            Color.Transparent,
            tokens.colors.accentStrong.copy(alpha = 0.16f),
        ),
    )
    val graphiteField = Brush.verticalGradient(
        colors = listOf(
            tokens.colors.surfaceElevated.copy(alpha = 0.92f),
            tokens.colors.background,
        ),
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 220.dp)
            .background(graphiteField)
            .background(steelWash)
            .alpha(enterAlpha.value),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = sectionPadding)
                .padding(top = NuvioTokens.Space.s24, bottom = NuvioTokens.Space.s28)
                .widthIn(max = 920.dp)
                .align(Alignment.CenterStart),
            verticalArrangement = Arrangement.spacedBy(NuvioTokens.Space.s12),
        ) {
            Text(
                text = stringResource(Res.string.app_brand_name),
                style = MaterialTheme.typography.displayLarge,
                color = tokens.colors.textPrimary,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = stringResource(Res.string.home_brand_headline),
                style = MaterialTheme.typography.headlineLarge,
                color = tokens.colors.textPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = stringResource(Res.string.home_brand_support),
                style = MaterialTheme.typography.bodyLarge,
                color = tokens.colors.textSecondary,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(NuvioTokens.Space.s8))
            Row(
                horizontalArrangement = Arrangement.spacedBy(NuvioTokens.Space.s12),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (onContinueClick != null) {
                    Button(
                        onClick = onContinueClick,
                        shape = tokens.shapes.button,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = tokens.colors.accent,
                            contentColor = tokens.colors.onAccent,
                        ),
                    ) {
                        Text(
                            text = stringResource(Res.string.home_cta_continue),
                            style = MaterialTheme.typography.labelLarge,
                            maxLines = 1,
                        )
                    }
                }
                if (onMultiViewerClick != null) {
                    OutlinedButton(
                        onClick = onMultiViewerClick,
                        shape = tokens.shapes.button,
                        border = BorderStroke(tokens.borders.thin, tokens.colors.borderDefault),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = tokens.colors.textSecondary,
                        ),
                    ) {
                        Text(
                            text = stringResource(Res.string.home_cta_multiview),
                            style = MaterialTheme.typography.labelLarge,
                            maxLines = 1,
                        )
                    }
                }
            }
        }
    }
}
