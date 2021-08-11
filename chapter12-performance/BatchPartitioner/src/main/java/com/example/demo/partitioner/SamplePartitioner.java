package com.example.demo.partitioner;

import java.util.HashMap;
import java.util.Map;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class SamplePartitioner implements Partitioner {

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {

        Map<String, ExecutionContext> map = new HashMap<>(gridSize);

        for (int i = 0; i < gridSize; i++) {
            ExecutionContext context = new ExecutionContext();
            context.put("sampleKey", "sampleValue" + i);

            map.put("partition" + i, context);
        }

        return map;
    }
}
