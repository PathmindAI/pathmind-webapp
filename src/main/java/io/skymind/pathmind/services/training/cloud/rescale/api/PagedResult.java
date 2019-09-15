package io.skymind.pathmind.services.training.cloud.rescale.api;

import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

class PagedResult<T> {
    @NotNull
    private int count;
    @Nullable
    private URI previous;
    @Nullable
    private URI next;
    @NotNull
    private List<T> results;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public URI getPrevious() {
        return previous;
    }

    public void setPrevious(URI previous) {
        this.previous = previous;
    }

    public URI getNext() {
        return next;
    }

    public void setNext(URI next) {
        this.next = next;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }
}
