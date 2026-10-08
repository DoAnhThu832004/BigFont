package com.thupo.bigfont.presentation.screen.custom

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.thupo.bigfont.R
import kotlinx.coroutines.flow.collectLatest
import android.provider.Settings
import android.widget.Toast
import androidx.compose.ui.graphics.Color

@Composable
fun CustomSizeScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    customSizeViewModel: CustomSizeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by customSizeViewModel.uiState.collectAsState()
    LaunchedEffect(customSizeViewModel.effectFlow) {
        customSizeViewModel.effectFlow.collectLatest { effect ->
            when (effect) {
                is CustomSizeUiEffect.NavigateBack -> onNavigateBack()
                is CustomSizeUiEffect.OpenSystemWriteSettings -> {
                    val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).apply {
                        data = Uri.parse("package:${context.packageName}")
                    }
                    context.startActivity(intent)
                }
                is CustomSizeUiEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    CustomSizeContent(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onScaleChanged = { scale ->
            customSizeViewModel.onEvent(CustomSizeUiEvent.OnScaleChanged(scale))
        },
        onApplyNow = {
            customSizeViewModel.onEvent(CustomSizeUiEvent.OnApplyNow)
        },
        onSaveCustomFont = {
            customSizeViewModel.onEvent(CustomSizeUiEvent.OnSaveCustomFont)
        },
        onDismissPermissionDialog = {
            customSizeViewModel.onEvent(CustomSizeUiEvent.OnDismissPermissionDialog)
        },
        onConfirmPermission = {
            customSizeViewModel.onEvent(CustomSizeUiEvent.OnConfirmPermission)
        },
        modifier = modifier
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSizeContent(
    uiState: CustomSizeUiState,
    onNavigateBack: () -> Unit,
    onScaleChanged: (Float) -> Unit,
    onApplyNow: () -> Unit,
    onSaveCustomFont: () -> Unit,
    onDismissPermissionDialog: () -> Unit,
    onConfirmPermission: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val currentDensity = LocalDensity.current
    CompositionLocalProvider(
        LocalDensity provides Density(density = currentDensity.density, fontScale = 1.0f)
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Tùy chỉnh cỡ chữ", fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                painter = painterResource(R.drawable.ic_back),
                                contentDescription = null,
                                tint = Color.Unspecified
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(20.dp)
            ) {
                Text(
                    text = "${uiState.percentage}%",
                    fontSize = 42.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Slider(
                    value = uiState.scale,
                    onValueChange = { newScale ->
                        onScaleChanged(newScale)
                    },
                    valueRange = 0.7f..2.8f,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("70% (Nhỏ)", style = MaterialTheme.typography.bodySmall)
                    Text("280% (Khổng lồ)", style = MaterialTheme.typography.bodySmall)
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Văn bản xem trước:",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.fillMaxWidth(),
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Aa Bb Cc 123\nĐây là cỡ chữ xem trước khi áp dụng vào điện thoại.",
                            fontSize = (18 * uiState.scale).sp,
                            lineHeight = (26 * uiState.scale).sp,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { onSaveCustomFont() },
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Lưu cỡ chữ", fontWeight = FontWeight.SemiBold)
                    }
                    Button(
                        onClick = { onApplyNow() },
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Áp dụng ngay", fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
    if (uiState.showPermissionDialog) {
        AlertDialog(
            onDismissRequest = { onDismissPermissionDialog() },
            title = { Text("Cần cấp quyền") },
            text = { Text("Ứng dụng cần quyền 'Sửa đổi cài đặt hệ thống' để áp dụng cỡ chữ bạn vừa chọn.") },
            confirmButton = {
                Button(onClick = { onConfirmPermission() }) {
                    Text("Cài đặt")
                }
            },
            dismissButton = {
                TextButton(onClick = { onDismissPermissionDialog() }) {
                    Text("Hủy")
                }
            }
        )
    }
}