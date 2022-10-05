@file:Suppress("TooManyFunctions")

package com.kinandcarta.create.proxytoggle.manager.view.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kinandcarta.create.proxytoggle.core.model.Proxy
import com.kinandcarta.create.proxytoggle.core.theme.BlueyGrey
import com.kinandcarta.create.proxytoggle.core.theme.ProxyToggleTheme
import com.kinandcarta.create.proxytoggle.core.theme.StatusLabelTextStyle
import com.kinandcarta.create.proxytoggle.manager.R
import com.kinandcarta.create.proxytoggle.manager.viewmodel.ProxyManagerViewModel
import com.kinandcarta.create.proxytoggle.manager.viewmodel.ProxyManagerViewModel.UiState
import com.kinandcarta.create.proxytoggle.manager.viewmodel.ProxyManagerViewModel.UiState.TextFieldState
import com.kinandcarta.create.proxytoggle.manager.viewmodel.ProxyManagerViewModel.UserInteraction

@Composable
fun ProxyManagerScreen(
    viewModel: ProxyManagerViewModel = viewModel(),
    useVerticalLayout: Boolean
) {
    val uiState by viewModel.uiState

    ProxyManagerScreenContent(
        useVerticalLayout = useVerticalLayout,
        uiState = uiState,
        onUserInteraction = viewModel::onUserInteraction,
        onForceFocusExecuted = viewModel::onForceFocusExecuted,
        onProxySelected = viewModel::onProxySelected
    )
}

@Composable
@Suppress("LongMethod")
fun ProxyManagerScreenContent(
    useVerticalLayout: Boolean,
    uiState: UiState,
    onUserInteraction: (UserInteraction) -> Unit,
    onForceFocusExecuted: () -> Unit,
    onProxySelected: (Proxy) -> Unit
) {
    @OptIn(ExperimentalComposeUiApi::class)
    if (uiState.proxyEnabled) {
        LocalSoftwareKeyboardController.current?.hide()
    }

    var showInfoDialog by rememberSaveable { mutableStateOf(false) }

    ProxyToggleTheme(darkTheme = uiState.darkTheme) {
        Surface {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                TopIcons(
                    onToggleTheme = { onUserInteraction(UserInteraction.ThemeToggled) },
                    onDismissInfo = { showInfoDialog = showInfoDialog.not() },
                    modifier = Modifier.align(Alignment.TopCenter)
                )
                MainLayout(
                    useVerticalLayout = useVerticalLayout,
                    buttonAndLabel = {
                        ButtonAndLabel(
                            proxyEnabled = uiState.proxyEnabled,
                            onToggleProxy = { onUserInteraction(UserInteraction.ProxyToggled) },
                            pastProxies = uiState.pastProxies,
                            onProxySelected = onProxySelected
                        )
                    },
                    addressTextField = {
                        ProxyToggleTextField(
                            label = stringResource(R.string.hint_ip_address),
                            state = uiState.addressState,
                            onTextChanged = { onUserInteraction(UserInteraction.AddressChanged(it)) },
                            enabled = uiState.proxyEnabled.not(),
                            keyboardOptions = getKeyboardOptions(KeyboardType.Uri, ImeAction.Next),
                            onForceFocusExecuted = onForceFocusExecuted
                        )
                    },
                    portTextField = {
                        ProxyToggleTextField(
                            label = stringResource(R.string.hint_port),
                            state = uiState.portState,
                            onTextChanged = { onUserInteraction(UserInteraction.PortChanged(it)) },
                            enabled = uiState.proxyEnabled.not(),
                            keyboardOptions = getKeyboardOptions(
                                KeyboardType.Number,
                                ImeAction.Done
                            ),
                            onForceFocusExecuted = onForceFocusExecuted
                        )
                    }
                )
                if (showInfoDialog) {
                    ProxyToggleAlertDialog(
                        message = stringResource(R.string.dialog_message_information),
                        onCloseDialog = { showInfoDialog = false }
                    )
                }
            }
        }
    }
}

@Composable
fun TopIcons(
    onToggleTheme: () -> Unit,
    onDismissInfo: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        ProxyToggleIcon(
            onClick = onToggleTheme,
            icon = R.drawable.ic_switch_theme,
            contentDescription = R.string.a11y_switch_theme
        )
        ProxyToggleIcon(
            onClick = onDismissInfo,
            icon = R.drawable.ic_info,
            contentDescription = R.string.a11y_information
        )
    }
}

@Composable
fun MainLayout(
    useVerticalLayout: Boolean,
    buttonAndLabel: @Composable () -> Unit,
    addressTextField: @Composable () -> Unit,
    portTextField: @Composable () -> Unit
) {
    if (useVerticalLayout) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.xlarge_margin))
        ) {
            buttonAndLabel()
            Spacer(Modifier.height(dimensionResource(R.dimen.large_margin)))
            addressTextField()
            Spacer(Modifier.height(dimensionResource(R.dimen.default_margin)))
            portTextField()
        }
    } else {
        Row(
            modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.xlarge_margin))
        ) {
            Column {
                Spacer(Modifier.height(dimensionResource(R.dimen.default_margin)))
                addressTextField()
                Spacer(Modifier.height(dimensionResource(R.dimen.default_margin)))
                portTextField()
            }
            Spacer(Modifier.width(dimensionResource(R.dimen.large_margin)))
            buttonAndLabel()
        }
    }
}

@Composable
fun ButtonAndLabel(
    proxyEnabled: Boolean,
    onToggleProxy: () -> Unit,
    pastProxies: List<Proxy>,
    onProxySelected: (Proxy) -> Unit
) {
    ConstraintLayout {
        val (button, text, dropdown) = createRefs()

        ProxyToggleButton(
            proxyEnabled = proxyEnabled,
            onClick = onToggleProxy,
            modifier = Modifier.constrainAs(button) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )

        val textMargin = dimensionResource(R.dimen.default_margin)
        Text(
            text = stringResource(
                if (proxyEnabled) R.string.connected else R.string.disconnected
            ).uppercase(),
            color = if (proxyEnabled) MaterialTheme.colors.primary else BlueyGrey,
            modifier = Modifier
                .constrainAs(text) {
                    top.linkTo(button.bottom, margin = textMargin)
                    start.linkTo(button.start)
                    end.linkTo(button.end)
                }
                .clearAndSetSemantics {},
            style = StatusLabelTextStyle
        )

        if (proxyEnabled.not() && pastProxies.isNotEmpty()) {
            var showDropDown by rememberSaveable { mutableStateOf(false) }
            IconButton(
                onClick = { showDropDown = showDropDown.not() },
                modifier = Modifier.constrainAs(dropdown) {
                    start.linkTo(text.end)
                    top.linkTo(text.top)
                    bottom.linkTo(text.bottom)
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.arrow_drop_down),
                    contentDescription = null,
                    tint = BlueyGrey
                )
            }
            DropdownMenu(expanded = showDropDown, onDismissRequest = { showDropDown = false }) {
                for (proxy in pastProxies) {
                    DropdownMenuItem(onClick = {
                        onProxySelected(proxy)
                        showDropDown = false
                    }) {
                        Text(text = proxy.toString())
                    }
                }
            }
        }
    }
}

private fun getKeyboardOptions(
    type: KeyboardType,
    imeAction: ImeAction
): KeyboardOptions {
    return KeyboardOptions.Default.copy(
        autoCorrect = false,
        keyboardType = type,
        imeAction = imeAction
    )
}

@Preview(name = "Connected (Light)", group = "ProxyManagerScreen")
@Composable
fun ProxyManagerScreenConnectedPreview() {
    ProxyManagerScreenForPreview(proxyEnabled = true)
}

@Preview(name = "Connected (Dark)", group = "ProxyManagerScreen")
@Composable
fun ProxyManagerScreenConnectedPreviewDark() {
    ProxyManagerScreenForPreview(darkTheme = true, proxyEnabled = true)
}

private val pastProxiesForPreview = listOf(
    Proxy("1.1.1.1", "1111"),
    Proxy("2.2.2.2", "2222")
)

@Preview(name = "Disconnected (Light)", group = "ProxyManagerScreen")
@Composable
fun ProxyManagerScreenDisconnectedPreview() {
    ProxyManagerScreenForPreview()
}

@Preview(name = "Disconnected (Dark)", group = "ProxyManagerScreen")
@Composable
fun ProxyManagerScreenDisconnectedPreviewDark() {
    ProxyManagerScreenForPreview(darkTheme = true)
}

@Preview(name = "Disconnected With Past Proxies (Light)", group = "ProxyManagerScreen")
@Composable
fun ProxyManagerScreenDisconnectedWithPastProxiesPreview() {
    ProxyManagerScreenForPreview(pastProxies = pastProxiesForPreview)
}

@Preview(name = "Disconnected With Past Proxies (Dark)", group = "ProxyManagerScreen")
@Composable
fun ProxyManagerScreenDisconnectedWithPastProxiesPreviewDark() {
    ProxyManagerScreenForPreview(darkTheme = true, pastProxies = pastProxiesForPreview)
}

private const val PREVIEW_LANDSCAPE_WIDTH = 800
private const val PREVIEW_LANDSCAPE_HEIGHT = 500

@Preview(
    name = "Connected (Light Landscape)", group = "ProxyManagerScreen Landscape",
    device = Devices.AUTOMOTIVE_1024p,
    widthDp = PREVIEW_LANDSCAPE_WIDTH,
    heightDp = PREVIEW_LANDSCAPE_HEIGHT
)
@Composable
fun ProxyManagerScreenConnectedPreviewLandscape() {
    ProxyManagerScreenForPreview(useVerticalLayout = false, proxyEnabled = true)
}

@Preview(
    name = "Connected (Dark Landscape)", group = "ProxyManagerScreen Landscape",
    device = Devices.AUTOMOTIVE_1024p,
    widthDp = PREVIEW_LANDSCAPE_WIDTH,
    heightDp = PREVIEW_LANDSCAPE_HEIGHT
)
@Composable
fun ProxyManagerScreenConnectedPreviewDarkLandscape() {
    ProxyManagerScreenForPreview(
        useVerticalLayout = false,
        darkTheme = true,
        proxyEnabled = true
    )
}

@Preview(
    name = "Disconnected (Light Landscape)", group = "ProxyManagerScreen Landscape",
    device = Devices.AUTOMOTIVE_1024p,
    widthDp = PREVIEW_LANDSCAPE_WIDTH,
    heightDp = PREVIEW_LANDSCAPE_HEIGHT
)
@Composable
fun ProxyManagerScreenDisconnectedPreviewLandscape() {
    ProxyManagerScreenForPreview(useVerticalLayout = false)
}

@Preview(
    name = "Disconnected (Dark Landscape)", group = "ProxyManagerScreen Landscape",
    device = Devices.AUTOMOTIVE_1024p,
    widthDp = PREVIEW_LANDSCAPE_WIDTH,
    heightDp = PREVIEW_LANDSCAPE_HEIGHT
)
@Composable
fun ProxyManagerScreenDisconnectedPreviewDarkLandscape() {
    ProxyManagerScreenForPreview(useVerticalLayout = false, darkTheme = true)
}

@Preview(
    name = "Disconnected With Past Proxies (Light Landscape)",
    group = "ProxyManagerScreen Landscape",
    device = Devices.AUTOMOTIVE_1024p,
    widthDp = PREVIEW_LANDSCAPE_WIDTH,
    heightDp = PREVIEW_LANDSCAPE_HEIGHT
)
@Composable
fun ProxyManagerScreenDisconnectedWithPastProxiesPreviewLandscape() {
    ProxyManagerScreenForPreview(useVerticalLayout = false, pastProxies = pastProxiesForPreview)
}

@Preview(
    name = "Disconnected With Past Proxies (Dark Landscape)",
    group = "ProxyManagerScreen Landscape",
    device = Devices.AUTOMOTIVE_1024p,
    widthDp = PREVIEW_LANDSCAPE_WIDTH,
    heightDp = PREVIEW_LANDSCAPE_HEIGHT
)
@Composable
fun ProxyManagerScreenDisconnectedWithPastProxiesPreviewDarkLandscape() {
    ProxyManagerScreenForPreview(
        useVerticalLayout = false,
        darkTheme = true,
        pastProxies = pastProxiesForPreview
    )
}

@Composable
private fun ProxyManagerScreenForPreview(
    useVerticalLayout: Boolean = true,
    darkTheme: Boolean = false,
    proxyEnabled: Boolean = false,
    pastProxies: List<Proxy> = emptyList()
) {
    val someProxy = Proxy("192.168.1.1", "8888")
    ProxyManagerScreenContent(
        useVerticalLayout = useVerticalLayout,
        uiState = UiState(
            darkTheme = darkTheme,
            proxyEnabled = proxyEnabled,
            addressState = TextFieldState(text = if (proxyEnabled) someProxy.address else ""),
            portState = TextFieldState(text = if (proxyEnabled) someProxy.port else ""),
            pastProxies = pastProxies
        ),
        onUserInteraction = {},
        onForceFocusExecuted = {},
        onProxySelected = {}
    )
}
