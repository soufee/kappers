package ru.kappers.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kappers.model.leonmodels.RunnerLeon;
import ru.kappers.repository.RunnerLeonRepository;
import ru.kappers.service.MarketLeonService;
import ru.kappers.service.OddsLeonService;
import ru.kappers.service.RunnerLeonService;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class RunnerLeonServiceImpl implements RunnerLeonService {
    private final RunnerLeonRepository repository;
    private final MarketLeonService marketService;
    private final OddsLeonService oddService;

    @Autowired
    public RunnerLeonServiceImpl(RunnerLeonRepository repository, MarketLeonService marketService, OddsLeonService oddService) {
        this.repository = repository;
        this.marketService = marketService;
        this.oddService = oddService;
    }

    @Override
    public List<RunnerLeon> getByMarketAndOdd(long marketId, long oddId) {
        return repository.getByMarketAndOdd(marketService.getById(marketId), oddService.getById(oddId));
    }

    @Override
    public List<RunnerLeon> getByOdd(long oddId) {
        return repository.getByOdd(oddService.getById(oddId));
    }

    @Override
    public List<RunnerLeon> getAll() {
        return repository.findAll();
    }

    @Override
    public RunnerLeon save(RunnerLeon runner) {
        return repository.save(runner);
    }

    @Override
    public void delete(RunnerLeon runner) {
        repository.delete(runner);
    }

    @Override
    public RunnerLeon getById(long id) {
        return repository.getOne(id);
    }

    @Override
    public void deleteAllByOddId(long oddId) {
        repository.deleteAllByOdd(oddService.getById(oddId));
    }

    @Override
    public List<RunnerLeon> saveAll(List<RunnerLeon> runners) {
        List<RunnerLeon> savedOnes = new ArrayList<>();
        for (RunnerLeon r:runners) {
            savedOnes.add(repository.save(r));
        }
       return savedOnes;
    }
}
