package com.lotusreichhart.gencanvas.feature.auth.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.lotusreichhart.gencanvas.core.common.util.TextResource
import com.lotusreichhart.gencanvas.core.common.R as CoreR
import com.lotusreichhart.gencanvas.core.ui.constant.Dimension
import com.lotusreichhart.gencanvas.feature.auth.R

@Composable
internal fun TermsAndPrivacyText(
    modifier: Modifier = Modifier,
    textBeforeTerms: TextResource = TextResource.Id(R.string.signup_terms_prefix),
    textAfter: TextResource = TextResource.Id(R.string.signup_terms_suffix),
    baseTextColor: Color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
    linkColor: Color = MaterialTheme.colorScheme.onBackground,
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit
) {

    val resolvedBefore = textBeforeTerms.asString()
    val resolvedAfter = textAfter.asString()

    val linkStyle = SpanStyle(
        color = linkColor,
        fontWeight = FontWeight.Bold,
        textDecoration = TextDecoration.Underline
    )

    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = baseTextColor)) {
            append("$resolvedBefore ")
        }

        val termsLink = LinkAnnotation.Clickable(
            tag = "terms",
            styles = TextLinkStyles(style = linkStyle),
            linkInteractionListener = {
                onTermsClick()
            }
        )
        withLink(termsLink) {
            append(stringResource(id = CoreR.string.core_label_terms_of_service))
        }

        withStyle(style = SpanStyle(color = baseTextColor)) {
            append(" ${stringResource(id = CoreR.string.core_and)} ")
        }

        val privacyLink = LinkAnnotation.Clickable(
            tag = "privacy",
            styles = TextLinkStyles(style = linkStyle),
            linkInteractionListener = {
                onPrivacyClick()
            }
        )
        withLink(privacyLink) {
            append(stringResource(id = CoreR.string.core_label_privacy_policy))
        }

        withStyle(style = SpanStyle(color = baseTextColor)) {
            append(" $resolvedAfter")
        }
    }

    Text(
        text = annotatedString,
        modifier = modifier,
        style = MaterialTheme.typography.bodySmall.copy(
            textAlign = TextAlign.Start,
            fontSize = Dimension.TextSize.bodyMedium,
            lineHeight = 23.sp
        )
    )
}
