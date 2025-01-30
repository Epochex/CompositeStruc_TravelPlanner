package com.traveloffersystem.persistence;

/**
 * Operator 接口：定义操作器的基本方法
 * 包含初始化和获取下一个结果的方法
 */
public interface Operator {
    /**
     * 初始化操作器，执行必要的预处理
     *
     * @throws Exception 初始化过程中可能抛出的异常
     */
    void init() throws Exception;

    /**
     * 获取下一个结果。如果没有更多结果，返回 null
     *
     * @return 下一个结果对象，或 null
     * @throws Exception 获取过程中可能抛出的异常
     */
    Object next() throws Exception;
}
