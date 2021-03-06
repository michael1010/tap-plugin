/*
 * The MIT License
 *
 * Copyright (c) <2012> <Bruno P. Kinoshita>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.tap4j.plugin;

import hudson.model.AbstractBuild;
import hudson.tasks.junit.CaseResult;
import hudson.tasks.test.AbstractTestResultAction;
import hudson.tasks.test.TestResult;

import java.util.Collections;
import java.util.List;

import org.kohsuke.stapler.StaplerProxy;
import org.kohsuke.stapler.export.Exported;
import org.tap4j.plugin.model.TapStreamResult;

/**
 * 
 * @author Bruno P. Kinoshita - http://www.kinoshita.eti.br
 * @since 0.1
 */
public class TapTestResultAction extends AbstractTestResultAction<AbstractTestResultAction<?>> implements StaplerProxy {

	private final AbstractBuild<?, ?> owner;
	private final TapResult tapResult;
	
	/**
	 * @param owner
	 * @param tapResult
	 */
	protected TapTestResultAction(AbstractBuild<?, ?> owner, TapResult tapResult) {
		super(owner);
		this.owner = owner;
		this.tapResult = tapResult;
	}
	
	/**
	 * @return the owner
	 */
	public AbstractBuild<?, ?> getOwner() {
		return owner;
	}
	
	/**
	 * @return the tapResult
	 */
	public TapResult getTapResult() {
		return tapResult;
	}

	/* (non-Javadoc)
	 * @see hudson.tasks.test.AbstractTestResultAction#getFailCount()
	 */
	@Override
	@Exported(visibility = 2)
	public int getFailCount() {
		return tapResult.getFailed();
	}

	/* (non-Javadoc)
	 * @see hudson.tasks.test.AbstractTestResultAction#getTotalCount()
	 */
	@Override
	@Exported(visibility = 2)
	public int getTotalCount() {
		return tapResult.getTotal();
	}
	
	/* (non-Javadoc)
	 * @see hudson.tasks.test.AbstractTestResultAction#getSkipCount()
	 */
	@Override
	@Exported(visibility = 2)
	public int getSkipCount() {
		return tapResult.getSkipped();
	}

	/* (non-Javadoc)
	 * @see hudson.tasks.test.AbstractTestResultAction#getFailedTests()
	 */
	@Override
	public List<CaseResult> getFailedTests() {
		//throw new AssertionError("Not supposed to be called");
		return Collections.emptyList();
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.kohsuke.stapler.StaplerProxy#getTarget()
	 */
	public Object getTarget() {
		return getResult();
	}
	
	/*
     * (non-Javadoc)
	 * @see hudson.tasks.test.AbstractTestResultAction#getResult()
	 */
	@Override
	public TestResult getResult() {
		return new TapStreamResult(owner, tapResult);
	}

	/*
	 * (non-Javadoc)
	 * @see hudson.tasks.test.AbstractTestResultAction#getPreviousResult()
	 */
	@Override
    public AbstractTestResultAction<?> getPreviousResult() {
        AbstractBuild<?, ?> previousBuild = owner.getPreviousBuild();
        if (previousBuild != null) {
            TapTestResultAction previousBuildAction = previousBuild.getAction(TapTestResultAction.class);
            return previousBuildAction;
        }
        return new EmptyTapTestResultAction(owner);
    }

	/* (non-Javadoc)
	 * @see hudson.tasks.test.AbstractTestResultAction#getUrlName()
	 */
	@Override
	@Exported(visibility = 2)
	public String getUrlName() {
		return "tapTestReport";
	}

	/* (non-Javadoc)
	 * @see hudson.tasks.test.AbstractTestResultAction#getDisplayName()
	 */
	@Override
	public String getDisplayName() {
		return "TAP Test Results";
	}

}

class EmptyTapTestResultAction extends TapTestResultAction { 
    
    private final AbstractBuild<?, ?> owner;
    
    /**
     * @param owner
     * @param tapResult
     */
    protected EmptyTapTestResultAction(AbstractBuild<?, ?> owner) {
        super(owner, null);
        this.owner = owner;
    }
    
    /*
     * (non-Javadoc)
     * @see org.tap4j.plugin.TapTestResultAction#getOwner()
     */
    public AbstractBuild<?, ?> getOwner() {
        return owner;
    }
    
    /* (non-Javadoc)
     * @see hudson.tasks.test.AbstractTestResultAction#getFailCount()
     */
    @Override
    @Exported(visibility = 2)
    public int getFailCount() {
        return 0;
    }

    /* (non-Javadoc)
     * @see hudson.tasks.test.AbstractTestResultAction#getTotalCount()
     */
    @Override
    @Exported(visibility = 2)
    public int getTotalCount() {
        return 0;
    }
    
    /* (non-Javadoc)
     * @see hudson.tasks.test.AbstractTestResultAction#getSkipCount()
     */
    @Override
    @Exported(visibility = 2)
    public int getSkipCount() {
        return 0;
    }

    /* (non-Javadoc)
     * @see hudson.tasks.test.AbstractTestResultAction#getFailedTests()
     */
    @Override
    public List<CaseResult> getFailedTests() {
        //throw new AssertionError("Not supposed to be called");
        return Collections.emptyList();
    }
    
    /*
     * (non-Javadoc)
     * @see hudson.tasks.test.AbstractTestResultAction#getPreviousResult()
     */
    @Override
    public AbstractTestResultAction<?> getPreviousResult() {
        AbstractBuild<?, ?> previousBuild = owner.getPreviousBuild();
        if (previousBuild != null) {
            TapTestResultAction previousBuildAction = previousBuild.getAction(TapTestResultAction.class);
            return previousBuildAction;
        }
        return null;
    }

    /* (non-Javadoc)
     * @see hudson.tasks.test.AbstractTestResultAction#getUrlName()
     */
    @Override
    @Exported(visibility = 2)
    public String getUrlName() {
        return "tapTestReport";
    }

    /* (non-Javadoc)
     * @see hudson.tasks.test.AbstractTestResultAction#getDisplayName()
     */
    @Override
    public String getDisplayName() {
        return "TAP Test Results";
    }
    
}
