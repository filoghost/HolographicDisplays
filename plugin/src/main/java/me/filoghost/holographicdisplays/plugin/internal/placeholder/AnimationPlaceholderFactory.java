/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.internal.placeholder;

import me.filoghost.fcommons.collection.CaseInsensitiveHashMap;
import me.filoghost.fcommons.collection.CaseInsensitiveMap;
import me.filoghost.holographicdisplays.api.placeholder.Placeholder;
import me.filoghost.holographicdisplays.api.placeholder.PlaceholderFactory;

import java.util.Map;

public class AnimationPlaceholderFactory implements PlaceholderFactory {

    private final CaseInsensitiveMap<Placeholder> animationsByFileName;

    public AnimationPlaceholderFactory(Map<String, AnimationPlaceholder> animationsByFileName) {
        this.animationsByFileName = new CaseInsensitiveHashMap<>();
        this.animationsByFileName.putAllString(animationsByFileName);
    }

    @Override
    public Placeholder getPlaceholder(String fileNameArgument) {
        Placeholder placeholder = animationsByFileName.get(fileNameArgument);
        if (placeholder != null) {
            return placeholder;
        } else {
            return new StaticPlaceholder("[Animation not found: " + fileNameArgument + "]");
        }
    }

}
