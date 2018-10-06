package com.sorta.fsn.core.security.exception;

import com.sorta.fsn.common.exception.error.BaseBusinessModuleException;
import com.sorta.fsn.common.exception.error.DefaultError;
import com.sorta.fsn.common.exception.error.IError;

/**
 * Created by Jim on 5/8/16.
 */
public class BaseSecurityException extends BaseBusinessModuleException
{
   private static final long serialVersionUID = 5962448653978019661L;

   public BaseSecurityException()
   {
      super();
      error = DefaultError.INVALID_TOKEN;
   }

   public BaseSecurityException(String s)
   {
      super(s);
      this.error = DefaultError.INVALID_TOKEN;
   }

   public BaseSecurityException(String s, Throwable throwable)
   {
      super(s, throwable);
      this.error = DefaultError.INVALID_TOKEN;
   }

   public BaseSecurityException(Throwable throwable)
   {
      super(throwable);
      this.error = DefaultError.INVALID_TOKEN;
   }
}
