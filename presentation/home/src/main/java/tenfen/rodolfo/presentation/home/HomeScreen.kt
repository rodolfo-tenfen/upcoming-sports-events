package tenfen.rodolfo.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import tenfen.rodolfo.presentation.eventitem.EventItem
import tenfen.rodolfo.presentation.eventitem.EventItemState
import tenfen.rodolfo.presentation.sportitem.SportItem
import tenfen.rodolfo.presentation.sportitem.SportItemState
import tenfen.rodolfo.presentation.theme.UpcomingSportsEventsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(title: String) {
    val viewModel = koinViewModel<HomeViewModel>()

    val state by viewModel.state.observeAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text(title) }) }
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)

        when (state) {
            null, HomeState.Loading -> HomeScreenLoading(modifier)
            is HomeState.Content ->
                HomeScreenContent(
                    state = state as HomeState.Content,
                    viewModel = viewModel,
                    modifier = modifier
                )
            HomeState.Empty -> HomeScreenEmpty(modifier)
            HomeState.Error -> HomeScreenError(onRetry = viewModel::reload, modifier)
        }
    }
}

@Composable
private fun HomeScreenLoading(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(modifier.size(100.dp), color = MaterialTheme.colorScheme.outline)
    }
}

@Composable
private fun HomeScreenContent(
    state: HomeState.Content,
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(minSize = 86.dp)
    ) {
        state.homeItemStates.forEachIndexed { sportIndex, state ->
            sport(state, viewModel, sportIndex)

            if (state.sportItemState.isExpanded)
                events(state, viewModel, sportIndex)
        }
    }
}

private fun LazyGridScope.sport(state: HomeItemState, viewModel: HomeViewModel, sportIndex: Int) {
    item(span = { GridItemSpan(maxLineSpan) }, contentType = SportItemState::class) {
        SportItem(
            sportItemState = state.sportItemState,
            onIsFilterActiveChange = { isActive ->
                viewModel.updateIsFilterActive(sportIndex, isActive)
            },
            onIsExpandedChange = { isExpanded ->
                viewModel.updateIsExpanded(sportIndex, isExpanded)
            },
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

private fun LazyGridScope.events(state: HomeItemState, viewModel: HomeViewModel, sportIndex: Int) {
    itemsIndexed(
        state.eventItemStates,
        contentType = { _: Int, _: EventItemState -> EventItemState::class }
    ) { eventIndex, eventItemState ->
        EventItem(
            eventItemState = eventItemState,
            onIsFavoritedChange = { isFavorited ->
                viewModel.updateIsFavorited(sportIndex, eventIndex, isFavorited)
            },
            modifier = Modifier.padding(8.dp),
            isFavoriteToggleEnabled = !state.sportItemState.isFilterActive
        )
    }
}

@Composable
private fun HomeScreenEmpty(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = stringResource(R.string.home_no_sports_events_found))
    }
}

@Composable
private fun HomeScreenError(onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.home_error_message),
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.Bold
            )

            FilledTonalButton(onClick = onRetry) {
                Text(text = stringResource(R.string.home_retry_button))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenLoadingPreview() {
    UpcomingSportsEventsTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            HomeScreenLoading()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenEmptyPreview() {
    UpcomingSportsEventsTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            HomeScreenEmpty(modifier = Modifier.fillMaxSize())
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenErrorPreview() {
    UpcomingSportsEventsTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            HomeScreenError(onRetry = {}, modifier = Modifier.fillMaxSize())
        }
    }
}
