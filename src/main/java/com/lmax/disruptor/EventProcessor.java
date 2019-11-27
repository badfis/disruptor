/*
 * Copyright 2011 LMAX Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lmax.disruptor;

/**
 * An EventProcessor needs to be an implementation of a runnable that will poll for events from the {@link RingBuffer}
 * using the appropriate wait strategy.  It is unlikely that you will need to implement this interface yourself.
 * Look at using the {@link EventHandler} interface along with the pre-supplied BatchEventProcessor in the first
 * instance.
 * <p>
 * An EventProcessor will generally be associated with a Thread for execution.
 */

/**
 * 消费端核心类。
 *
 *disruptor在启动的时候会将所有注册上来的EventProcessor提交到线程池中执行，因此一个EventProcessor可以看成一个独立的线程流，用于处理RingBuffer上的数据.
 *
 * 其实现类实现了run()方法，不断的轮询，获取数据对象，把数据对象交给消费者处理，具体怎么交给消费者，利用了消费者的等待策略；
 *
 * 共有3个实现类：1、BatchEventProcessor,2、WorkProcessor,3、NoOpEventProcessor
 *
 * 1、BatchEventProcessor在run()方法中回调com.lmax.disruptor.EventHandler的实现对象，其所有的Consumer都实现了com.lmax.disruptor.EventHandler接口；
 *
 * 2、WorkProcessor在run()方法中回调com.lmax.disruptor.WorkHandler的实现对象，其所有的Consumer都实现了com.lmax.disruptor.WorkHandler接口；在多生产者多消费者模式下，确保每个sequence只被一个processor消费，在同一个WorkPool中，
 * 确保多个WorkProcessor不会消费同样的sequence。在WorkerPool中使用了WorkProcessor，WorkerPool会把传入的workhandler放入workProcessor中。
 *
 * 3.NoOpEventProcessor啥都没做
 *
 */
public interface EventProcessor extends Runnable
{
    /**
     * Get a reference to the {@link Sequence} being used by this {@link EventProcessor}.
     *
     * @return reference to the {@link Sequence} for this {@link EventProcessor}
     */
    Sequence getSequence();

    /**
     * Signal that this EventProcessor should stop when it has finished consuming at the next clean break.
     * It will call {@link SequenceBarrier#alert()} to notify the thread to check status.
     */
    void halt();

    boolean isRunning();
}
