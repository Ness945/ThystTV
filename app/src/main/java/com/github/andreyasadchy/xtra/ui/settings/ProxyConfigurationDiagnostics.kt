package com.github.andreyasadchy.xtra.ui.settings

/**
 * Reports whether this app's optional proxy settings have enough local configuration to run.
 *
 * This deliberately inspects preferences only. It does not send a probe, persist credentials,
 * or make any system-wide network, DNS, VPN, or ad-filtering claim.
 */
internal data class ProxyConfiguration(
    val playlistProxyEnabled: Boolean,
    val playlistUrl: String?,
    val playbackAccessTokenProxyEnabled: Boolean,
    val multivariantPlaylistProxyEnabled: Boolean,
    val mediaPlaylistProxyEnabled: Boolean,
    val httpProxyHost: String?,
    val httpProxyPort: String?,
)

internal enum class ProxyConfigurationDiagnostic {
    DIRECT,
    PLAYLIST_PROXY_INCOMPLETE,
    HTTP_PROXY_INCOMPLETE,
    PLAYLIST_PROXY_READY,
    HTTP_PROXY_READY,
    BOTH_PROXIES_READY,
}

internal fun ProxyConfiguration.diagnostic(): ProxyConfigurationDiagnostic {
    val playlistEnabled = playlistProxyEnabled
    val httpOptionsEnabled = playbackAccessTokenProxyEnabled ||
        multivariantPlaylistProxyEnabled || mediaPlaylistProxyEnabled
    val httpConfigured = !httpProxyHost.isNullOrBlank() || !httpProxyPort.isNullOrBlank()
    val httpEnabled = httpOptionsEnabled && httpConfigured
    val playlistReady = !playlistUrl.isNullOrBlank()
    val httpReady = !httpProxyHost.isNullOrBlank() &&
        (httpProxyPort?.toIntOrNull()?.let { it in 1..65535 } == true)

    return when {
        playlistEnabled && !playlistReady -> ProxyConfigurationDiagnostic.PLAYLIST_PROXY_INCOMPLETE
        httpEnabled && !httpReady -> ProxyConfigurationDiagnostic.HTTP_PROXY_INCOMPLETE
        playlistEnabled && httpEnabled -> ProxyConfigurationDiagnostic.BOTH_PROXIES_READY
        playlistEnabled -> ProxyConfigurationDiagnostic.PLAYLIST_PROXY_READY
        httpEnabled -> ProxyConfigurationDiagnostic.HTTP_PROXY_READY
        else -> ProxyConfigurationDiagnostic.DIRECT
    }
}
