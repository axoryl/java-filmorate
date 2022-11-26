package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.util.ExceptionThrowHandler;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaServiceImpl implements MpaService {

    private final MpaDao mpaDao;

    @Override
    public List<Mpa> getAll() {
        return mpaDao.findAll();
    }

    @Override
    public Mpa getById(Long id) {
        final Mpa mpa = mpaDao.findById(id);
        ExceptionThrowHandler.throwExceptionIfMpaNotExists(mpa);

        return mpa;
    }
}
