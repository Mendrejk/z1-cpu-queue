# cpu queue simulator
A basic simulation of three queuing algorithms (FCFS, SJF, RR) that are used by CPU to handle incoming requests.

FCFS is most optimal for datasets with some very long requests - usage of other algorithms leads to starving of those.  
SJF is most optimal for average dataset when it comes to lowest average time in queue, but leads to starvation of requests.  
RR is most optimal for datasets with not too many processes in the queue at the same time - it struggles with too many
    large processes as it will starve all of them by only completing fractions of them and then finishing them all at
    the same time.