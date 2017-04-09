#!/usr/bin/env ruby

thread_range = 1..16
size_options = [7_000_000, 70_000_000, 700_000_000]

size_options.each do |size|
    thread_range.each do |thread_count|
        # Run the jar 3 times and find the average runtime
        timings = 3.times.map {
            `java -Xms512M -Xmx4G -jar Assignment1.jar -NUMTHREADS #{thread_count} -DATASIZE #{size} -VERBOSE 0`.
                strip.gsub(/ms/, '').to_i
        }
        average_perf = timings.reduce(:+) / timings.size

        puts "#{size},#{thread_count},#{average_perf}"
    end
end
