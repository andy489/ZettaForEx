package com.zetta.forex.util;

import com.zetta.forex.model.dto.AllRatesResponseDto;
import com.zetta.forex.model.entity.ExchangeRatesEntity;

public class TestConstants {

    public static final String BASE_CURR_CODE = "EUR";
    public static final String CURR_EPOCH_TIME = "1748433361";

    public static final AllRatesResponseDto ALL_RATES_RESPONSE_DTO = new AllRatesResponseDto()
            .setSuccess(true)
            .setTimestamp(CURR_EPOCH_TIME)
            .setSource(BASE_CURR_CODE)
            .setQuotes(CurrencyMapCreator.createCurrencyMap());

    public static final String currencyJson = "{\"EURAED\":4.159407,\"EURAFN\":79.268412,\"EURALL\":97.982377," +
            "\"EURAMD\":435.710355,\"EURANG\":2.02669,\"EURAOA\":1038.998421,\"EURARS\":1309.652174," +
            "\"EURAUD\":1.757071,\"EURAWG\":2.039794,\"EURAZN\":1.92508,\"EURBAM\":1.954053,\"EURBBD\":2.288354," +
            "\"EURBDT\":138.386245,\"EURBGN\":1.956498,\"EURBHD\":0.426984,\"EURBIF\":3373.398151," +
            "\"EURBMD\":1.132432,\"EURBND\":1.459994,\"EURBOB\":7.831996,\"EURBRL\":6.397902,\"EURBSD\":1.133386," +
            "\"EURBTC\":1.0383762E-5,\"EURBTN\":96.74848,\"EURBWP\":15.213395,\"EURBYN\":3.709083," +
            "\"EURBYR\":22195.672976,\"EURBZD\":2.276564,\"EURCAD\":1.564835,\"EURCDF\":3244.418907," +
            "\"EURCHF\":0.936747,\"EURCLF\":0.027709,\"EURCLP\":1063.308976,\"EURCNY\":8.158378," +
            "\"EURCOP\":4657.694028,\"EURCRC\":576.579288,\"EURCUC\":1.132432,\"EURCUP\":30.009456," +
            "\"EURCVE\":110.166481,\"EURCZK\":24.906757,\"EURDJF\":201.829509,\"EURDKK\":7.456897," +
            "\"EURDOP\":66.789682,\"EURDZD\":149.646466,\"EUREGP\":56.396489,\"EURERN\":16.986484," +
            "\"EURETB\":154.076453,\"EURFJD\":2.558674,\"EURFKP\":0.838704,\"EURGBP\":0.838261,\"EURGEL\":3.103177," +
            "\"EURGGP\":0.838704,\"EURGHS\":11.73051,\"EURGIP\":0.838704,\"EURGMD\":81.535469,\"EURGNF\":9818.263128," +
            "\"EURGTQ\":8.696223,\"EURGYD\":237.47763,\"EURHKD\":8.876814,\"EURHNL\":29.52034,\"EURHRK\":7.536109," +
            "\"EURHTG\":148.088876,\"EURHUF\":403.363323,\"EURIDR\":18473.820996,\"EURILS\":4.004818," +
            "\"EURIMP\":0.838704,\"EURINR\":96.60972,\"EURIQD\":1484.672295,\"EURIRR\":47703.710852," +
            "\"EURISK\":144.000167,\"EURJEP\":0.838704,\"EURJMD\":180.277191,\"EURJOD\":0.802906," +
            "\"EURJPY\":163.142158,\"EURKES\":146.314193,\"EURKGS\":99.030653,\"EURKHR\":4536.942728," +
            "\"EURKMF\":492.045133,\"EURKPW\":1019.153149,\"EURKRW\":1551.217025,\"EURKWD\":0.347589," +
            "\"EURKYD\":0.944455,\"EURKZT\":580.171168,\"EURLAK\":24491.098236,\"EURLBP\":101550.286171," +
            "\"EURLKR\":339.796129,\"EURLRD\":226.677288,\"EURLSL\":20.285259,\"EURLTL\":3.343777," +
            "\"EURLVL\":0.684997,\"EURLYD\":6.175477,\"EURMAD\":10.462144,\"EURMDL\":19.533532," +
            "\"EURMGA\":5175.371797,\"EURMKD\":61.52268,\"EURMMK\":2377.681221,\"EURMNT\":4050.076909," +
            "\"EURMOP\":9.150817,\"EURMUR\":51.662191,\"EURMVR\":17.507273,\"EURMWK\":1965.242534," +
            "\"EURMXN\":21.848018,\"EURMYR\":4.791351,\"EURMZN\":72.373537,\"EURNAD\":20.285259," +
            "\"EURNGN\":1796.252829,\"EURNIO\":41.712881,\"EURNOK\":11.496385,\"EURNPR\":154.796203," +
            "\"EURNZD\":1.896281,\"EUROMR\":0.435444,\"EURPAB\":1.133386,\"EURPEN\":4.144793,\"EURPGK\":4.6508," +
            "\"EURPHP\":62.835249,\"EURPKR\":320.744866,\"EURPLN\":4.243487,\"EURPYG\":9055.901538," +
            "\"EURQAR\":4.142595,\"EURRON\":5.053484,\"EURRSD\":117.218132,\"EURRUB\":90.111689," +
            "\"EURRWF\":1608.447403,\"EURSAR\":4.247834,\"EURSBD\":9.456678,\"EURSCR\":16.100393," +
            "\"EURSDG\":680.024231,\"EURSEK\":10.874798,\"EURSGD\":1.458358,\"EURSHP\":0.889914," +
            "\"EURSLL\":23746.539246,\"EURSOS\":647.715045,\"EURSRD\":41.71089,\"EURSTD\":23439.062129," +
            "\"EURSVC\":9.917131,\"EURSYP\":14723.701493,\"EURSZL\":20.278865,\"EURTHB\":36.969945," +
            "\"EURTJS\":11.424383,\"EURTMT\":3.969175,\"EURTND\":3.383974,\"EURTOP\":2.652272,\"EURTRY\":44.229793," +
            "\"EURTTD\":7.710105,\"EURTWD\":33.823259,\"EURTZS\":3054.739818,\"EURUAH\":47.255541," +
            "\"EURUGX\":4135.301905,\"EURUSD\":1.132432,\"EURUYU\":47.167819,\"EURUZS\":14665.884674," +
            "\"EURVND\":29395.11129,\"EURVUV\":137.126643,\"EURWST\":3.120514,\"EURXAF\":655.370919," +
            "\"EURXAG\":0.033987,\"EURXAU\":3.41E-4,\"EURXCD\":3.060455,\"EURXDR\":0.815071,\"EURXOF\":655.370919," +
            "\"EURXPF\":119.153444,\"EURYER\":276.143948,\"EURZAR\":20.282825,\"EURZMK\":10193.24597," +
            "\"EURZMW\":30.629609,\"EURZWL\":364.642737}";

    public static final ExchangeRatesEntity EXCHANGE_RATES_ENTITY = new ExchangeRatesEntity()
            .setId(1L)
            .setTimestamp(CURR_EPOCH_TIME)
            .setSource(BASE_CURR_CODE)
            .setQuotes(currencyJson);

    public static final double DELTA = 0.1E-10;
}
