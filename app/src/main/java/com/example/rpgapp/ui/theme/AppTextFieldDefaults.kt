package com.example.rpgapp.ui.theme

import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

/**
 * Cores padrão para todos os OutlinedTextField do app.
 * Usa as cores do tema atual automaticamente.
 *
 * Uso:
 *   OutlinedTextField(
 *       colors = AppTextFieldDefaults.colors(),
 *       ...
 *   )
 */
object AppTextFieldDefaults {

    @Composable
    fun colors(): TextFieldColors = OutlinedTextFieldDefaults.colors(
        // ── Borda ────────────────────────────────────────────
        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
        focusedBorderColor   = MaterialTheme.colorScheme.primary,
        errorBorderColor     = MaterialTheme.colorScheme.error,

        // ── Fundo ────────────────────────────────────────────
        unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f),
        focusedContainerColor   = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
        errorContainerColor     = MaterialTheme.colorScheme.errorContainer,

        // ── Texto ────────────────────────────────────────────
        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
        focusedTextColor   = MaterialTheme.colorScheme.onSurface,
        errorTextColor     = MaterialTheme.colorScheme.error,

        // ── Label ────────────────────────────────────────────
        unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
        focusedLabelColor   = MaterialTheme.colorScheme.primary,
        errorLabelColor     = MaterialTheme.colorScheme.error,

        // ── Placeholder ──────────────────────────────────────
        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
        focusedPlaceholderColor   = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),

        // ── Cursor ───────────────────────────────────────────
        cursorColor = MaterialTheme.colorScheme.primary,

        // ── Ícones ───────────────────────────────────────────
        unfocusedLeadingIconColor  = MaterialTheme.colorScheme.onSurfaceVariant,
        focusedLeadingIconColor    = MaterialTheme.colorScheme.primary,
        unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
        focusedTrailingIconColor   = MaterialTheme.colorScheme.primary,
    )
}