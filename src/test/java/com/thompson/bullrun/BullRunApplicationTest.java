package com.thompson.bullrun;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Fail.fail;

    @ExtendWith(MockitoExtension.class)
    class BullRunApplicationTest {

        @Test
        void testApplicationStartup() {
            try {
                BullRunApplication.main(new String[]{});
            } catch (Exception e) {
                fail("Application startup failed with exception: " + e.getMessage());
            }
        }
    }
