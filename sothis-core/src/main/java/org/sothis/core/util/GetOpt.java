package org.sothis.core.util;

/**
 * 一个简单的命令行参数工具
 * 
 * @author velna
 * 
 */
public class GetOpt {
	private int index;
	private String value;
	private String name;
	private final String[] argv;

	public GetOpt(String[] argv) {
		this.argv = argv;
	}

	/**
	 * 得到下一个待匹配的参数在argv中的索引
	 * 
	 * @return
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * 得到当前匹配的参数的值
	 * 
	 * @return
	 */
	public String getValue() {
		return value;
	}

	/**
	 * 得到当前匹配的参数名
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 得到下一下参数对
	 * 
	 * @return 返回-1表示没有更多的参数对，如果参数名长度为1返回参数名的字符，如果参数名长度大于1则返回0。
	 */
	public int next() {
		int ch = -1;
		this.name = null;
		this.value = null;
		while (ch == -1 && index < argv.length) {
			String s = argv[index++];
			if (s.startsWith("--")) {
				if (s.length() > 2) {
					int i = s.indexOf('=');
					ch = 0;
					if (i > 0) {
						this.name = s.substring(2, i);
						this.value = s.substring(i + 1);
					} else {
						this.name = s.substring(2);
					}
				}
			} else if (s.charAt(0) == '-') {
				if (s.length() > 1) {
					this.name = s.substring(1);
					if (this.name.length() == 1) {
						ch = this.name.charAt(0);
					} else {
						ch = 0;
					}
					if (index < argv.length && argv[index].charAt(0) != '-') {
						this.value = argv[index];
						index++;
					}
				}
			}
		}
		return ch;
	}

	public static void main(String[] args) {
		GetOpt getopt = new GetOpt(new String[] { "-h", "a", "--check=true", "abcde", "-list" });
		int ch;
		while ((ch = getopt.next()) != -1) {
			switch (ch) {
			case 'h':
				System.out.println("help=" + getopt.getValue());
				break;
			case 0:
				System.out.println(getopt.getName() + "=" + getopt.getValue());
				break;
			}
		}
	}
}
