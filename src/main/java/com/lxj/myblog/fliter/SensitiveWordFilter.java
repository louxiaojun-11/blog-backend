package com.lxj.myblog.fliter;

import java.util.*;

public class SensitiveWordFilter {

    private final Node root = new Node();

    // 初始化敏感词库
    public SensitiveWordFilter(Set<String> sensitiveWords) {
        for (String word : sensitiveWords) {
            addWord(word.toLowerCase());
        }
    }

    // 构建DFA树
    private void addWord(String word) {
        Node current = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            Node node = current.getChild(c);
            //如果当前字符节点不存在，则创建新节点并添加到子节点列表中
            if (node == null) {
                node = new Node();
                current.addChild(c, node);
            }
            
            current = node;
            
            if (i == word.length() - 1) {
                current.setEnd(true);
            }
        }
    }

    // 检测文本是否包含敏感词
    public boolean containsSensitiveWord(String text) {
        text = text.toLowerCase();
        for (int i = 0; i < text.length(); i++) {
            Node temp = root;
            
            for (int j = i; j < text.length(); j++) {
                char c = text.charAt(j);
                temp = temp.getChild(c);
                
                if (temp == null) {
                    break; // 当前路径不存在，退出循环
                }
                
                if (temp.isEnd()) {
                    return true; // 发现敏感词
                }
            }
        }
        return false;
    }

    // 获取文本中的所有敏感词
    public Set<String> getSensitiveWords(String text) {
        text = text.toLowerCase();
        Set<String> sensitiveWords = new HashSet<>();
        
        for (int i = 0; i < text.length(); i++) {
            Node temp = root;
            StringBuilder word = new StringBuilder();
            
            for (int j = i; j < text.length(); j++) {
                char c = text.charAt(j);
                temp = temp.getChild(c);
                
                if (temp == null) {
                    break;
                }
                
                word.append(c);
                
                if (temp.isEnd()) {
                    sensitiveWords.add(word.toString());
                    // 找到后可以继续查找更长的敏感词（如果需要最大匹配）
                    // 跳过已检测到的敏感词部分
                     i = j;
                     break;
                }
            }
        }
        return sensitiveWords;
    }

    // DFA节点类
    private static class Node {
        private final Map<Character, Node> children = new HashMap<>();
        private boolean isEnd;

        public Node getChild(Character c) {
            return children.get(c);
        }

        public void addChild(Character c, Node node) {
            children.put(c, node);
        }

        public boolean isEnd() {
            return isEnd;
        }

        public void setEnd(boolean end) {
            isEnd = end;
        }
    }

    // 使用示例
    public static void main(String[] args) {
        Set<String> sensitiveWords = new HashSet<>(Arrays.asList("中国", "敏感词", "测试"));
        SensitiveWordFilter filter = new SensitiveWordFilter(sensitiveWords);

        String text1 = "这是一篇正常的博文";
        String text2 = "这篇包含敏感词需要处理";
        String text3 = "这个测试中国内容敏感词";

        System.out.println("text1包含敏感词: " + filter.containsSensitiveWord(text1));
        System.out.println("text2包含敏感词: " + filter.containsSensitiveWord(text2));
        System.out.println("text3包含敏感词: " + filter.containsSensitiveWord(text3));
        
        System.out.println("text3中的敏感词: " + filter.getSensitiveWords(text3));
    }
}