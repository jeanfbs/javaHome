package br.com.javahome.loader;

import br.com.javahome.ui.HousePlanUI;

public interface LoaderSVG {

    HousePlanUI load(final String svgPath) throws Exception;
}
