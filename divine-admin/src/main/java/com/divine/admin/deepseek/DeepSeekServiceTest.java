//package com.divine.admin.deepseek;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//
//import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
//import static org.hibernate.validator.internal.util.Contracts.assertTrue;
//
///**
// * DeepSeek服务测试类
// */
//
//@SpringBootTest
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//public class DeepSeekServiceTest {
//
//    @Autowired
//    private DeepSeekService deepSeekService;
//    private DeepSeekConfig config;
//
//    @BeforeAll
//    public void setUp() {
//        config = new DeepSeekConfig();
//        // 使用测试环境的API Key
//        config.setApiKey(System.getenv("TEST_DEEPSEEK_API_KEY"));
//        config.setEnableVerboseLogging(false);
//
//        deepSeekService = new DeepSeekServiceImpl(config);
//    }
//
//    @Test
//    @DisplayName("测试API连接")
//    public void testConnection() {
//        assertTrue(deepSeekService.testConnection(), "API连接测试失败");
//    }
//
//    @Test
//    @DisplayName("测试简单聊天")
//    public void testSimpleChat() {
//        String response = deepSeekService.simpleChat("你好");
//        assertNotNull(response, "聊天响应不能为空");
//        assertFalse(response.trim().isEmpty(), "聊天响应内容不能为空");
//        System.out.println("测试回复: " + response);
//    }
//
//    @Test
//    @DisplayName("测试带系统提示的聊天")
//    public void testChatWithSystemPrompt() {
//        String response = deepSeekService.chatWithSystemPrompt(
//            "你是一个翻译助手，只做中英文翻译",
//            "Hello, how are you?"
//        );
//
//        assertNotNull(response);
//        System.out.println("翻译结果: " + response);
//    }
//
//    @Test
//    @DisplayName("测试配置获取")
//    public void testGetConfig() {
//        DeepSeekConfig serviceConfig = deepSeekService.getConfig();
//        assertNotNull(serviceConfig);
//        assertEquals(config.getApiBaseUrl(), serviceConfig.getApiBaseUrl());
//    }
//
//    @Test
//    @DisplayName("测试统计信息")
//    public void testGetUsageStatistics() {
//        String stats = deepSeekService.getUsageStatistics();
//        assertNotNull(stats);
//        assertTrue(stats.contains("总请求数"),"总请求数");
//        System.out.println("统计信息:\n" + stats);
//    }
//
//
//    @AfterAll
//    void tearDown() {
//        if (deepSeekService != null) {
//            deepSeekService.shutdown();
//        }
//    }
//}
