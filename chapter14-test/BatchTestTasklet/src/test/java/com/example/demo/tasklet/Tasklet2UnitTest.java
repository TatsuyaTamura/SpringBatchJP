package com.example.demo.tasklet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.repeat.RepeatStatus;
import com.example.demo.component.SampleComponent;
import lombok.extern.slf4j.Slf4j;

@ExtendWith(MockitoExtension.class)
@Slf4j
@DisplayName("Tasklet2のUnitTest")
public class Tasklet2UnitTest {

    @InjectMocks
    private Tasklet2 tasklet2;

    @Mock
    private SampleComponent component;
    
    @BeforeAll
    public static void initAll() {
        log.info("=== Tasklet2 UnitTest Start ===");
    }
    
    @AfterAll
    public static void tearDownAll() {
        log.info("=== Tasklet2 UnitTest End ===");
    }
    
    @Test
    @DisplayName("RepeatStatusがFINISHEDで終了すること")
    public void checkRepeatStatus() throws Exception {
        // テスト
        RepeatStatus repeatStatus = tasklet2.execute(null, null);
        // 検証
        assertThat(repeatStatus).isNotNull();
        assertThat(repeatStatus).isEqualTo(RepeatStatus.FINISHED);
    }
    
    @Test
    @DisplayName("ランダム値が10であること")
    public void checkRandomValue() throws Exception {
        // 準備
        when(component.random()).thenReturn(10); // 戻り値のセット
        // テスト
        tasklet2.execute(null, null);
        // 検証
        assertThat(tasklet2.getRandomValue()).isEqualTo(10);
    }
}
