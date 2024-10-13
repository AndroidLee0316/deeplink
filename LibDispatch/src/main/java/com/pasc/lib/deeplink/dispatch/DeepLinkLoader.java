
package com.pasc.lib.deeplink.dispatch;

import com.pasc.lib.deeplink.dispatch.bean.DeepLinkEntry;
import com.pasc.lib.deeplink.dispatch.loader.Parser;

import java.util.ArrayList;
import java.util.List;

public final class DeepLinkLoader implements Parser {
    public static final List<DeepLinkEntry> REGISTRY = new ArrayList<>();

    public static void register(List<DeepLinkEntry> deepLinkEntries) {
        REGISTRY.clear();
        REGISTRY.addAll(deepLinkEntries);
    }

    @Override
    public DeepLinkEntry parseUri(String uri) {
        for (DeepLinkEntry entry : REGISTRY) {
            if (entry.matches(uri)) {
                return entry;
            }
        }
        return null;
    }
}
