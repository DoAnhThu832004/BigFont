package com.thupo.bigfont.presentation.screen.home

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.thupo.bigfont.domain.model.FontScaleItem
import com.thupo.bigfont.presentation.components.FontScaleCard
import com.thupo.bigfont.presentation.components.HomeQuickActions
import kotlinx.coroutines.flow.collectLatest
import com.thupo.bigfont.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToSettings: () -> Unit = {},
    onNavigateToCustomSize: () -> Unit = {},
    onNavigateToMagnifier: () -> Unit = {}
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.onEvent(HomeUiEvent.RefreshData)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
    LaunchedEffect(viewModel.effectFlow) {
        viewModel.effectFlow.collectLatest { effect ->
            when (effect) {
                is HomeUiEffect.OpenSystemWriteSettings -> {
                    val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).apply {
                        data = Uri.parse("package:${context.packageName}")
                    }
                    context.startActivity(intent)
                }
                is HomeUiEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
                is HomeUiEffect.NavigateToCustomSize -> onNavigateToCustomSize()
                is HomeUiEffect.NavigateToMagnifier -> onNavigateToMagnifier()
            }
        }
    }

    val currentDensity = LocalDensity.current
    CompositionLocalProvider(
        LocalDensity provides Density(
            density = currentDensity.density,
            fontScale = 1.0f
        )
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "BIG FONT",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    },
                    actions = {
                        IconButton(onClick = onNavigateToSettings) {
                            Icon(
                                painter = painterResource(R.drawable.ic_setting),
                                contentDescription = "Cài đặt",
                                tint = Color.Unspecified
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 1. Hai nút chức năng hàng trên (Custom Size & Kính lúp)
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    HomeQuickActions(
                        onCustomSizeClick = { viewModel.onEvent(HomeUiEvent.OnClickCustomSize) },
                        onMagnifierClick = { viewModel.onEvent(HomeUiEvent.OnClickMagnifier) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // 2. Danh sách các mức Font Scale
                items(uiState.fontScales, key = { it.id }) { item ->
                    FontScaleCard(
                        item = item,
                        onSelect = { viewModel.onEvent(HomeUiEvent.OnSelectScale(item)) },
                        onDelete = if (item.isCustom) {
                            { viewModel.onEvent(HomeUiEvent.OnDeleteCustomScale(item)) }
                        } else null
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }

    // Dialog xin quyền WRITE_SETTINGS
    if (uiState.showPermissionDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(HomeUiEvent.OnDismissPermissionDialog) },
            title = { Text(text = "Yêu cầu cấp quyền") },
            text = {
                Text(
                    text = "Để thay đổi kích thước chữ toàn hệ thống, ứng dụng cần quyền 'Sửa đổi cài đặt hệ thống'. Vui lòng cho phép quyền này trong Cài đặt."
                )
            },
            confirmButton = {
                Button(onClick = { viewModel.onEvent(HomeUiEvent.OnConfirmRequestPermission) }) {
                    Text("Cài đặt")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onEvent(HomeUiEvent.OnDismissPermissionDialog) }) {
                    Text("Hủy")
                }
            }
        )
    }
}