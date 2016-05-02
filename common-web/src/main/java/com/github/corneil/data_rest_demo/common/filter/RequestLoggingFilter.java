package com.github.corneil.data_rest_demo.common.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Enumeration;

/**
 * Created by Corneil on 2016/04/01.
 */
public class RequestLoggingFilter implements Filter {
    private static class BufferedRequestWrapper extends HttpServletRequestWrapper {
        byte[] buffer;
        public BufferedRequestWrapper(HttpServletRequest req) throws IOException {
            super(req);
            InputStream is = req.getInputStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte buf[] = new byte[8192];
            while (true) {
                int len = is.read(buf);
                if (len > 0) {
                    bos.write(buf, 0, len);
                } else if (len < 0) {
                    break;
                }
            }
            buffer = bos.toByteArray();
        }
        public byte[] getBuffer() {
            return buffer;
        }
        public ServletInputStream getInputStream() {
            try {
                return new BufferedServletInputStream(new ByteArrayInputStream(buffer));
            } catch (Exception ex) {
                logger.error("getInputStream:" + ex, ex);
            }
            return null;
        }
    }

    private static class BufferedServletInputStream extends ServletInputStream {
        private ByteArrayInputStream bis;
        private boolean finished = false;
        private ReadListener readListener = null;
        public BufferedServletInputStream(ByteArrayInputStream bis) {
            this.bis = bis;
        }
        @Override
        public boolean isFinished() {
            return finished;
        }
        @Override
        public boolean isReady() {
            return bis.available() > 0;
        }
        @Override
        public void setReadListener(ReadListener listener) {
            if (readListener != null) {
                throw new IllegalStateException("listener already set");
            }
            readListener = listener;
        }
        public int read() throws IOException {
            int len = bis.read();
            if (len == -1) {
                finished = true;
                if (readListener != null) {
                    readListener.onAllDataRead();
                }
            }
            if (readListener != null && bis.available() > 0) {
                readListener.onDataAvailable();
            }
            return len;
        }
        public int read(byte[] buf, int off, int len) throws IOException {
            int l = bis.read(buf, off, len);
            if (l == -1) {
                finished = true;
                if (readListener != null) {
                    readListener.onAllDataRead();
                }
            }
            if (readListener != null && bis.available() > 0) {
                readListener.onDataAvailable();
            }
            return l;
        }
        public int available() {
            return bis.available();
        }
    }

    private static class ByteArrayPrintWriter {
        private ByteArrayOutputStream bos = new ByteArrayOutputStream();
        private PrintWriter pw = new PrintWriter(bos);
        private ServletOutputStream sos = new ByteArrayServletStream(bos);
        public ServletOutputStream getStream() {
            return sos;
        }
        public PrintWriter getWriter() {
            return pw;
        }
        byte[] toByteArray() {
            pw.flush();
            return bos.toByteArray();
        }
    }

    private static class ByteArrayServletStream extends ServletOutputStream {
        private ByteArrayOutputStream bos;
        private PrintStream pos;
        private WriteListener writeListener = null;
        ByteArrayServletStream(ByteArrayOutputStream bos) {
            this.bos = bos;
            this.pos = new PrintStream(bos);
        }
        @Override
        public void print(String s) throws IOException {
            pos.print(s);
            if (writeListener != null) {
                writeListener.onWritePossible();
            }
        }
        @Override
        public void print(boolean b) throws IOException {
            pos.print(b);
            if (writeListener != null) {
                writeListener.onWritePossible();
            }
        }
        @Override
        public void print(char c) throws IOException {
            pos.print(c);
            if (writeListener != null) {
                writeListener.onWritePossible();
            }
        }
        @Override
        public void print(int i) throws IOException {
            pos.print(i);
            if (writeListener != null) {
                writeListener.onWritePossible();
            }
        }
        @Override
        public void print(long l) throws IOException {
            pos.print(l);
            if (writeListener != null) {
                writeListener.onWritePossible();
            }
        }
        @Override
        public void print(float f) throws IOException {
            pos.print(f);
            if (writeListener != null) {
                writeListener.onWritePossible();
            }
        }
        @Override
        public void print(double d) throws IOException {
            pos.print(d);
            if (writeListener != null) {
                writeListener.onWritePossible();
            }
        }
        @Override
        public void println() throws IOException {
            pos.println();
            if (writeListener != null) {
                writeListener.onWritePossible();
            }
        }
        @Override
        public void println(String x) throws IOException {
            pos.println(x);
            if (writeListener != null) {
                writeListener.onWritePossible();
            }
        }
        @Override
        public void println(boolean x) throws IOException {
            pos.println(x);
            if (writeListener != null) {
                writeListener.onWritePossible();
            }
        }
        @Override
        public void println(char x) throws IOException {
            pos.println(x);
            if (writeListener != null) {
                writeListener.onWritePossible();
            }
        }
        @Override
        public void println(int x) throws IOException {
            pos.println(x);
            if (writeListener != null) {
                writeListener.onWritePossible();
            }
        }
        @Override
        public void println(long x) throws IOException {
            pos.println(x);
            if (writeListener != null) {
                writeListener.onWritePossible();
            }
        }
        @Override
        public void println(float x) throws IOException {
            pos.println(x);
            if (writeListener != null) {
                writeListener.onWritePossible();
            }
        }
        @Override
        public void println(double x) throws IOException {
            pos.println(x);
            if (writeListener != null) {
                writeListener.onWritePossible();
            }
        }
        @Override
        public boolean isReady() {
            return true;
        }
        @Override
        public void setWriteListener(WriteListener listener) {
            if (writeListener != null) {
                throw new IllegalStateException("writeListener already set");
            }
            this.writeListener = listener;
        }
        @Override
        public void write(int b) throws IOException {
            pos.write(b);
            if (writeListener != null) {
                writeListener.onWritePossible();
            }
        }
        @Override
        public void write(byte[] buf, int off, int len) throws IOException {
            pos.write(buf, off, len);
            if (writeListener != null) {
                writeListener.onWritePossible();
            }
        }
        @Override
        public void flush() {
            pos.flush();
        }
        @Override
        public void close() {
            pos.close();
        }
    }

    private static Logger logger = LoggerFactory.getLogger("requestFilter");
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("init: {}", filterConfig.getFilterName());
    }
    @Override
    public void doFilter(ServletRequest sRequest, ServletResponse sResponse, FilterChain chain) throws IOException, ServletException {
        try {
            if (logger.isDebugEnabled()) {
                final HttpServletRequest httpRequest = (HttpServletRequest) sRequest;
                BufferedRequestWrapper bufferedRequest = new BufferedRequestWrapper(httpRequest);
                logger.debug("REMOTE: {}", httpRequest.getRemoteAddr());
                logger.debug("REQUEST METHOD: {}", httpRequest.getMethod());
                logger.debug("REQUEST URL: {}", httpRequest.getRequestURL().toString());
                logger.debug("REQUEST HEADERS -> ");
                Enumeration<String> headers = httpRequest.getHeaderNames();
                while (headers.hasMoreElements()) {
                    String name = headers.nextElement();
                    Enumeration<String> values = httpRequest.getHeaders(name);
                    while (values.hasMoreElements()) {
                        logger.debug("\t{}={}", name, values.nextElement());
                    }
                }
                logger.debug("REQUEST PARAMETERS ->");
                if (!httpRequest.getParameterMap().isEmpty()) {
                    Enumeration<String> names = httpRequest.getParameterNames();
                    while (names.hasMoreElements()) {
                        String name = names.nextElement();
                        String[] values = httpRequest.getParameterValues(name);
                        if (values != null) {
                            if (values.length == 1) {
                                logger.debug("\t{}={}", name, values[0]);
                            } else {
                                logger.debug("\t{}={}", name, Arrays.asList(values));
                            }
                        } else {
                            logger.debug("\t{}={}", name, httpRequest.getParameter(name));
                        }
                    }
                }
                logger.debug("REQUEST BODY -> {}", new String(bufferedRequest.getBuffer()));
                final HttpServletResponse response = (HttpServletResponse) sResponse;
                final ByteArrayPrintWriter pw = new ByteArrayPrintWriter();
                HttpServletResponse wrappedResp = new HttpServletResponseWrapper(response) {
                    public ServletOutputStream getOutputStream() {
                        return pw.getStream();
                    }
                    public PrintWriter getWriter() {
                        return pw.getWriter();
                    }
                };
                chain.doFilter(bufferedRequest, wrappedResp);
                byte[] bytes = pw.toByteArray();
                response.getOutputStream().write(bytes);
                logger.debug("RESPONSE HEADERS ->");
                for (String name : wrappedResp.getHeaderNames()) {
                    logger.debug("\t{}={}", name, wrappedResp.getHeaders(name));
                }
                logger.debug("RESPONSE -> {}", new String(bytes));
            } else {
                chain.doFilter(sRequest, sResponse);
            }
        } catch (IOException x) {
            logger.error("filter:" + x, x);
            throw x;
        } catch (ServletException x) {
            logger.error("filter:" + x, x);
            throw x;
        } catch (Throwable x) {
            logger.error("filter:" + x, x);
            throw new ServletException(x);
        }
    }
    @Override
    public void destroy() {
    }
}