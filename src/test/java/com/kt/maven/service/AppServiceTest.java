package com.kt.maven.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessResourceFailureException;

import com.kt.maven.models.Reco;
import com.kt.maven.repository.AppRepository;

@ExtendWith(MockitoExtension.class)
class AppServiceTest {

    @Mock
    private AppRepository repo;

    private AppService service;

    @BeforeEach
    void setUp() {
        service = new AppService(repo);
    }

    @Test
    void createRecord_passesRecordToRepository() {
        Reco record = new Reco(1, "First record");

        service.createRecord(record);

        verify(repo).save(record);
    }

    @Test
    void createRecord_propagatesSaveFailure() {
        Reco record = new Reco(1, "First record");
        var failure = new DataAccessResourceFailureException(
                "Database unavailable"
        );
        when(repo.save(record)).thenThrow(failure);

        var thrown = assertThrows(
                DataAccessResourceFailureException.class,
                () -> service.createRecord(record)
        );

        assertSame(failure, thrown);
    }

    @Test
    void getAllRecords_returnsRepositoryRecords() {
        Reco first = new Reco(1, "First record");
        Reco second = new Reco(2, "Second record");
        when(repo.findAll()).thenReturn(List.of(first, second));

        List<Reco> result = service.getAllRecords();

        assertEquals(List.of(first, second), result);
    }

    @Test
    void getAllRecords_returnsEmptyListWhenNoRecordsExist() {
        when(repo.findAll()).thenReturn(List.of());

        List<Reco> result = service.getAllRecords();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllRecords_propagatesReadFailure() {
        var failure = new DataAccessResourceFailureException(
                "Database unavailable"
        );
        when(repo.findAll()).thenThrow(failure);

        var thrown = assertThrows(
                DataAccessResourceFailureException.class,
                () -> service.getAllRecords()
        );

        assertSame(failure, thrown);
    }
}