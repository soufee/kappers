package ru.kappers.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ru.kappers.exceptions.CurrRateGettingException;
import ru.kappers.model.CurrencyRate;
import ru.kappers.service.CurrRateService;
import ru.kappers.service.parser.CBRFDailyCurrencyRatesParser;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CurrencyServiceImplTest {
    @InjectMocks
    private CurrencyServiceImpl currencyService;
    @Mock
    private CurrRateService currRateService;
    @Mock
    private CBRFDailyCurrencyRatesParser currencyRatesParser;

    @Test
    public void refreshCurrencyRatesForTodayMustSaveRateIfTableHasNotRate() {
        final Date currentDate = Date.valueOf(LocalDate.now());
        final String usdCharCode = "USD";
        final CurrencyRate currencyRate = CurrencyRate.builder()
                .date(currentDate)
                .charCode(usdCharCode)
                .build();
        final List<CurrencyRate> currencyRates = Arrays.asList(currencyRate);
        when(currencyRatesParser.parseFromCBRF()).thenReturn(currencyRates);
        when(currRateService.isExist(currentDate, usdCharCode)).thenReturn(false);

        currencyService.refreshCurrencyRatesForToday();

        verify(currencyRatesParser).parseFromCBRF();
        verify(currRateService).isExist(currentDate, usdCharCode);
        verify(currRateService).save(currencyRate);
    }

    @Test
    public void refreshCurrencyRatesForTodayIfTableHasRate() {
        final Date currentDate = Date.valueOf(LocalDate.now());
        final String usdCharCode = "USD";
        final CurrencyRate currencyRate = CurrencyRate.builder()
                .date(currentDate)
                .charCode(usdCharCode)
                .build();
        final List<CurrencyRate> currencyRates = Arrays.asList(currencyRate);
        when(currencyRatesParser.parseFromCBRF()).thenReturn(currencyRates);
        when(currRateService.isExist(currentDate, usdCharCode)).thenReturn(true);

        currencyService.refreshCurrencyRatesForToday();

        verify(currencyRatesParser).parseFromCBRF();
        verify(currRateService).isExist(currentDate, usdCharCode);
        verify(currRateService, never()).save(currencyRate);
    }

    @Test(expected = CurrRateGettingException.class)
    public void refreshCurrencyRatesForTodayMustThrowExceptionIfParserThrowAnyException() {
        when(currencyRatesParser.parseFromCBRF()).thenThrow(RuntimeException.class);

        currencyService.refreshCurrencyRatesForToday();
    }
}