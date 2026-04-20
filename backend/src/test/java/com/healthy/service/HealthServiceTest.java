package com.healthy.service;

import com.healthy.entity.DietRecord;
import com.healthy.entity.HealthIndicator;
import com.healthy.entity.SleepRecord;
import com.healthy.entity.SportRecord;
import com.healthy.mapper.DietRecordMapper;
import com.healthy.mapper.HealthIndicatorMapper;
import com.healthy.mapper.SleepRecordMapper;
import com.healthy.mapper.SportRecordMapper;
import com.healthy.service.impl.HealthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HealthServiceTest {

    @Mock
    private HealthIndicatorMapper healthIndicatorMapper;
    @Mock
    private SportRecordMapper sportRecordMapper;
    @Mock
    private DietRecordMapper dietRecordMapper;
    @Mock
    private SleepRecordMapper sleepRecordMapper;

    @InjectMocks
    private HealthServiceImpl healthService;

    // ========== HealthIndicator CRUD ==========

    @Test
    void testGetIndicatorList_Success() {
        HealthIndicator ind = new HealthIndicator();
        ind.setId(1L);
        ind.setUserId(1L);
        when(healthIndicatorMapper.selectByUserId(1L)).thenReturn(List.of(ind));

        List<HealthIndicator> result = healthService.getIndicatorList(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        verify(healthIndicatorMapper, times(1)).selectByUserId(1L);
    }

    @Test
    void testGetIndicatorList_Empty() {
        when(healthIndicatorMapper.selectByUserId(999L)).thenReturn(List.of());

        List<HealthIndicator> result = healthService.getIndicatorList(999L);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void testSaveIndicator_Insert() {
        HealthIndicator record = new HealthIndicator();
        record.setUserId(1L);
        record.setWeight(65.5);
        record.setRecordDate(LocalDate.now());
        doNothing().when(healthIndicatorMapper).insert(any(HealthIndicator.class));

        assertDoesNotThrow(() -> healthService.saveIndicator(record));
        verify(healthIndicatorMapper, times(1)).insert(any(HealthIndicator.class));
    }

    @Test
    void testSaveIndicator_Update() {
        HealthIndicator record = new HealthIndicator();
        record.setId(1L);
        record.setUserId(1L);
        record.setWeight(70.0);
        doNothing().when(healthIndicatorMapper).update(any(HealthIndicator.class));

        assertDoesNotThrow(() -> healthService.saveIndicator(record));
        verify(healthIndicatorMapper, times(1)).update(any(HealthIndicator.class));
    }

    @Test
    void testDeleteIndicator_Success() {
        doNothing().when(healthIndicatorMapper).deleteById(1L);

        assertDoesNotThrow(() -> healthService.deleteIndicator(1L));
        verify(healthIndicatorMapper, times(1)).deleteById(1L);
    }

    // ========== SportRecord CRUD ==========

    @Test
    void testGetSportList_Success() {
        SportRecord sport = new SportRecord();
        sport.setId(1L);
        sport.setUserId(1L);
        when(sportRecordMapper.selectByUserId(1L)).thenReturn(List.of(sport));

        List<SportRecord> result = healthService.getSportList(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(sportRecordMapper, times(1)).selectByUserId(1L);
    }

    @Test
    void testGetSportList_Empty() {
        when(sportRecordMapper.selectByUserId(999L)).thenReturn(List.of());

        List<SportRecord> result = healthService.getSportList(999L);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void testSaveSport_Insert() {
        SportRecord record = new SportRecord();
        record.setUserId(1L);
        record.setSportType("跑步");
        record.setDuration(30);
        doNothing().when(sportRecordMapper).insert(any(SportRecord.class));

        assertDoesNotThrow(() -> healthService.saveSport(record));
        verify(sportRecordMapper, times(1)).insert(any(SportRecord.class));
    }

    @Test
    void testSaveSport_Update() {
        SportRecord record = new SportRecord();
        record.setId(1L);
        record.setUserId(1L);
        record.setSportType("游泳");
        record.setDuration(45);
        doNothing().when(sportRecordMapper).update(any(SportRecord.class));

        assertDoesNotThrow(() -> healthService.saveSport(record));
        verify(sportRecordMapper, times(1)).update(any(SportRecord.class));
    }

    @Test
    void testDeleteSport_Success() {
        doNothing().when(sportRecordMapper).deleteById(1L);

        assertDoesNotThrow(() -> healthService.deleteSport(1L));
        verify(sportRecordMapper, times(1)).deleteById(1L);
    }

    // ========== DietRecord CRUD ==========

    @Test
    void testGetDietList_Success() {
        DietRecord diet = new DietRecord();
        diet.setId(1L);
        diet.setUserId(1L);
        when(dietRecordMapper.selectByUserId(1L)).thenReturn(List.of(diet));

        List<DietRecord> result = healthService.getDietList(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(dietRecordMapper, times(1)).selectByUserId(1L);
    }

    @Test
    void testGetDietList_Empty() {
        when(dietRecordMapper.selectByUserId(999L)).thenReturn(List.of());

        List<DietRecord> result = healthService.getDietList(999L);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void testSaveDiet_Insert() {
        DietRecord record = new DietRecord();
        record.setUserId(1L);
        record.setMealType("早餐");
        record.setFoodName("面包");
        doNothing().when(dietRecordMapper).insert(any(DietRecord.class));

        assertDoesNotThrow(() -> healthService.saveDiet(record));
        verify(dietRecordMapper, times(1)).insert(any(DietRecord.class));
    }

    @Test
    void testSaveDiet_Update() {
        DietRecord record = new DietRecord();
        record.setId(1L);
        record.setUserId(1L);
        record.setMealType("午餐");
        record.setFoodName("米饭");
        doNothing().when(dietRecordMapper).update(any(DietRecord.class));

        assertDoesNotThrow(() -> healthService.saveDiet(record));
        verify(dietRecordMapper, times(1)).update(any(DietRecord.class));
    }

    @Test
    void testDeleteDiet_Success() {
        doNothing().when(dietRecordMapper).deleteById(1L);

        assertDoesNotThrow(() -> healthService.deleteDiet(1L));
        verify(dietRecordMapper, times(1)).deleteById(1L);
    }

    // ========== SleepRecord CRUD ==========

    @Test
    void testGetSleepList_Success() {
        SleepRecord sleep = new SleepRecord();
        sleep.setId(1L);
        sleep.setUserId(1L);
        when(sleepRecordMapper.selectByUserId(1L)).thenReturn(List.of(sleep));

        List<SleepRecord> result = healthService.getSleepList(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(sleepRecordMapper, times(1)).selectByUserId(1L);
    }

    @Test
    void testGetSleepList_Empty() {
        when(sleepRecordMapper.selectByUserId(999L)).thenReturn(List.of());

        List<SleepRecord> result = healthService.getSleepList(999L);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void testSaveSleep_Insert() {
        SleepRecord record = new SleepRecord();
        record.setUserId(1L);
        record.setQuality("优");
        doNothing().when(sleepRecordMapper).insert(any(SleepRecord.class));

        assertDoesNotThrow(() -> healthService.saveSleep(record));
        verify(sleepRecordMapper, times(1)).insert(any(SleepRecord.class));
    }

    @Test
    void testSaveSleep_Update() {
        SleepRecord record = new SleepRecord();
        record.setId(1L);
        record.setUserId(1L);
        record.setQuality("良");
        doNothing().when(sleepRecordMapper).update(any(SleepRecord.class));

        assertDoesNotThrow(() -> healthService.saveSleep(record));
        verify(sleepRecordMapper, times(1)).update(any(SleepRecord.class));
    }

    @Test
    void testDeleteSleep_Success() {
        doNothing().when(sleepRecordMapper).deleteById(1L);

        assertDoesNotThrow(() -> healthService.deleteSleep(1L));
        verify(sleepRecordMapper, times(1)).deleteById(1L);
    }
}
