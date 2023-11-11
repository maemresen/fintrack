package com.maemresen.fintrack.persistence.repository.base;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseRepository<E, I> extends JpaRepository<E, I> {
}
