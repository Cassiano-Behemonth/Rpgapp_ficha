package com.example.rpgapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rpgapp.viewmodel.FichaAssimilacaoViewModel
import kotlinx.coroutines.launch

@Composable
fun FichaAssimilacaoScreen(
    viewModel: FichaAssimilacaoViewModel,
    onThemeChange: () -> Unit,
    onModeChange: () -> Unit
) {
    val tabs = listOf("Ficha", "Aptidões", "Assimilações", "Inventário", "Características", "Descrição")
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val scope = rememberCoroutineScope()
    var selectedTab by remember { mutableStateOf(0) }

    // Sincronizar tab com pager
    LaunchedEffect(selectedTab) {
        pagerState.animateScrollToPage(selectedTab)
    }
    LaunchedEffect(pagerState.currentPage) {
        selectedTab = pagerState.currentPage
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            edgePadding = 0.dp,
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = {
                        selectedTab = index
                        scope.launch { pagerState.animateScrollToPage(index) }
                    },
                    text = {
                        Text(
                            title,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 13.sp
                        )
                    }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            when (page) {
                0 -> FichaAssimilacaoTab(
                    viewModel = viewModel,
                    onThemeChange = onThemeChange,
                    onModeChange = onModeChange
                )
                1 -> AptidoesAssimilacaoScreen(viewModel = viewModel)
                2 -> AssimilacoesScreen(viewModel = viewModel)
                3 -> InventarioAssimilacaoScreen(viewModel = viewModel)
                4 -> CaracteristicasAssimilacaoScreen(viewModel = viewModel)
                5 -> DescricaoAssimilacaoScreen(viewModel = viewModel)
            }
        }
    }
}