package com.github.andreyasadchy.xtra.ui.settings

import org.junit.Assert.assertEquals
import org.junit.Test

class ProxyConfigurationDiagnosticsTest {
    @Test fun `reports direct playback when no app proxy option is enabled`() {
        assertEquals(ProxyConfigurationDiagnostic.DIRECT, configuration().diagnostic())
        assertEquals(
            ProxyConfigurationDiagnostic.DIRECT,
            configuration(mediaPlaylistProxyEnabled = true).diagnostic(),
        )
    }

    @Test fun `reports an incomplete playlist proxy without a URL`() {
        assertEquals(
            ProxyConfigurationDiagnostic.PLAYLIST_PROXY_INCOMPLETE,
            configuration(playlistProxyEnabled = true, playlistUrl = " ").diagnostic(),
        )
    }

    @Test fun `reports an incomplete HTTP proxy without a valid host and port`() {
        assertEquals(
            ProxyConfigurationDiagnostic.HTTP_PROXY_INCOMPLETE,
            configuration(
                mediaPlaylistProxyEnabled = true,
                httpProxyHost = "proxy.example",
                httpProxyPort = "0",
            ).diagnostic(),
        )
    }

    @Test fun `reports each configured proxy path`() {
        assertEquals(
            ProxyConfigurationDiagnostic.PLAYLIST_PROXY_READY,
            configuration(playlistProxyEnabled = true, playlistUrl = "https://example.test/playlist").diagnostic(),
        )
        assertEquals(
            ProxyConfigurationDiagnostic.HTTP_PROXY_READY,
            configuration(mediaPlaylistProxyEnabled = true, httpProxyHost = "proxy.example", httpProxyPort = "8080").diagnostic(),
        )
        assertEquals(
            ProxyConfigurationDiagnostic.BOTH_PROXIES_READY,
            configuration(
                playlistProxyEnabled = true,
                playlistUrl = "https://example.test/playlist",
                mediaPlaylistProxyEnabled = true,
                httpProxyHost = "proxy.example",
                httpProxyPort = "8080",
            ).diagnostic(),
        )
    }

    private fun configuration(
        playlistProxyEnabled: Boolean = false,
        playlistUrl: String? = null,
        playbackAccessTokenProxyEnabled: Boolean = false,
        multivariantPlaylistProxyEnabled: Boolean = false,
        mediaPlaylistProxyEnabled: Boolean = false,
        httpProxyHost: String? = null,
        httpProxyPort: String? = null,
    ) = ProxyConfiguration(
        playlistProxyEnabled,
        playlistUrl,
        playbackAccessTokenProxyEnabled,
        multivariantPlaylistProxyEnabled,
        mediaPlaylistProxyEnabled,
        httpProxyHost,
        httpProxyPort,
    )
}
