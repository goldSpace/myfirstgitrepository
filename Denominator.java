package web;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * <h1>求最大公约数<h1><br>
 * 质因数分解法 <br>
 * 短除法 <br>
 * 辗转相除法 <br>
 * 更相减损法
 * 
 * @author yugi111
 */
public class Denominator
{
	public static void main ( String[] args )
	{
		int[] numbers = { 121, 121, 131 };
		try
		{
			System.out.println (Arrays.toString (numbers) + " 的最大公约数是：\n");
			System.out.println ("辗转相除法: " + euclidean (1, -1, -1, numbers));
			System.out.println ("更相减损法: " + derogateLaw (1, -1, -1, 0, numbers));
			System.out.println ("短除法: " + shortDivision (numbers));
			System.out.println ("质因数分解法: " + factorization (0, null, null, numbers));
		}
		catch (Exception e)
		{
			System.err.println (e.toString ());
			System.exit (-1);
		}
	}

	/**
	 * 辗转相除法：<br>
	 * <p>
	 * 求两个自然数(非负整数)的最大公约数的一种方法, 也叫欧几里德算法<br>
	 * <br>
	 * 用辗转相除法求几个数的最大公约数，可以先求出其中任意两个数的最大公约数，<br>
	 * 再求这个最大公约数与第三个数的最大公约数，依次求下去，直到最后一个数为止。<br>
	 * 最后所得的那个最大公约数，就是所有这些数的最大公约数
	 * 
	 * @param start
	 *            int 记录到第几个numbers里面的数字
	 * @param a
	 *            int 计算的第一个数字
	 * @param b
	 *            int 计算的第二个数字
	 * @param numbers
	 *            int... 包括待计算的数字的数组
	 * @throws Exception
	 *             参数不合法时抛出异常
	 */
	public static int euclidean ( int start, int a, int b, int... numbers ) throws Exception
	{
		int[] temp = isLegaled (a, b, numbers);
		a = temp[0];
		b = temp[1];
		int mod = a % b;
		if (mod == 0)
		{
			start++;
			if (start < numbers.length)
			{
				return euclidean (start, b, numbers[start], numbers);
			}
			else
			{
				return b;
			}
		}
		else
		{
			return euclidean (start, b, mod, numbers);
		}
	}

	/**
	 * 质因数分解法<br>
	 * 1. 把每个数分别分解质因数<br>
	 * 2. 再把各数中的全部公有质因数提取出来连乘<br>
	 * <p>
	 * 所得的积就是这几个数的最大公约数
	 * 
	 * @param start
	 *            int 表示已经到达克隆数组的第几个数字
	 * @param map
	 *            Map&lt;Integer, String&gt; 存放所有数字的分解因数
	 * @param cloned
	 *            int[] 存放被克隆的待计算的数字数组
	 * @param numbers
	 *            int... 存放原始待计算的数字数组
	 * @return factorization int 返回重复计算的最大公约数
	 * @throws Exception
	 *             参数不够异常检测
	 */
	public static int factorization ( int start, Map<Integer, String> map, int[] cloned, int... numbers )
			throws Exception
	{
		isLegaled (-1, -1, numbers);
		cloned = null == cloned ? Arrays.copyOf (numbers, numbers.length) : cloned;
		Arrays.sort (cloned);
		if (null == map)// 初始化
		{
			if (( "," + Arrays.toString (numbers).replaceAll ("[\\[\\]\\s]+", "") + "," ).indexOf (",1,") != -1)
			{
				return 1;
			}
			map = new HashMap<Integer, String> ();
		}
		for ( int i = 0; i < cloned.length; i++ )
		{
			for ( int j = 2; j <= cloned[i]; j++ )
			{
				if (cloned[i] % j == 0)
				{
					cloned[i] = cloned[i] / j;
					String strx = null == map.get (i) ? "" : map.get (i);
					map.put (i, strx + ( j + "|" ));
					return factorization (i, map, cloned, numbers);
				}
			}
			if (i == cloned.length - 1)
			{
				Map<String, Integer> result = new HashMap<String, Integer> ();
				int answer = 1;
				List<String> temp = formatArray (map.get (1)); // 从1开始
				for ( int j = 0; j < map.size (); j++ ) // 整个比较
				{
					List<String> strs = formatArray (map.get (j));
					for ( int k = 0; k < temp.size (); k++ )
					{
						String tx = temp.get (k);
						int times = strs.toString ().split (tx).length - 1;
						if (null == result.get (tx)) // 开始时，放入任意一个
						{
							result.put (tx, times);
						}
						// 后面放入较小的一个
						else if (times < result.get (tx))
						{
							result.put (tx, times);
						}
					}
				}
				for ( Entry<String, Integer> entry : result.entrySet () )
				{
					answer *= Math.pow (Double.valueOf (entry.getKey ()), entry.getValue ());
				}
				return answer;
			}
		}
		return 1;
	}

	/**
	 * <h1>短除法</h1> <br>
	 * 先用这几个数的公约数连续去除，一直除到所有的商互质为止, <br>
	 * 然后把所有的除数连乘起来，所得的积就是这几个数的最大公约数
	 * 
	 * @param numbers
	 *            int... 存放待计算的数字数组
	 * @return shortDivision int 返回重复计算的最大公约数
	 * @throws Exception
	 *             参数不够抛出异常
	 */
	public static int shortDivision ( int... numbers ) throws Exception
	{
		isLegaled (-1, -1, numbers);
		int[] cloned = Arrays.copyOf (numbers, numbers.length);
		Arrays.sort (cloned);
		for ( int i = 2; i <= cloned[0]; i++ )
		{
			boolean divisible = true;
			for ( int clone : cloned )
			{
				if (clone % i != 0)
				{
					divisible = false;
				}
			}
			if (divisible)
			{
				for ( int j = 0; j < cloned.length; j++ )
				{
					cloned[j] = cloned[j] / i;
				}
				return i * shortDivision (cloned);
			}
		}
		return 1;
	}

	/**
	 * <h1>更相减损法</h1><br>
	 * 第一步：任意给定两个正整数；判断它们是否都是偶数。若是，则用2约简；若不是则执行第二步。<br>
	 * 第二步：以较大的数减较小的数，接着把所得的差与较小的数比较，并以大数减小数。<br>
	 * 继续这个操作，直到所得的减数和差相等为止。 <br>
	 * 则第一步中约掉的若干个2与第二步中等数的乘积就是所求的最大公约数。
	 * 
	 * @param start
	 *            int 当前处理到了第几个数组里面的数字
	 * @param a
	 *            int 计算的第一个数
	 * @param b
	 *            int 计算的第二个数
	 * @param count
	 *            int 被计算出来的几个2
	 * @param numbers
	 *            int... 待计算的数字数组
	 * @return derogateLaw int 重复计算的最大公约数
	 * @throws Exception
	 *             参数非法抛出异常
	 */
	public static int derogateLaw ( int start, int a, int b, int count, int... numbers ) throws Exception
	{
		int[] temp = isLegaled (a, b, numbers);
		a = temp[0];
		b = temp[1];
		if (a % 2 == 0 && b % 2 == 0)
		{
			return derogateLaw (start, a >> 1, b >> 1, ++count, numbers);
		}
		if (a % 2 != 0 || b % 2 != 0)
		{
			int difference = -1;
			if (( difference = Math.abs (a - b) ) != a && difference != 0 && difference != b)
			{
				b = a > b ? b : a;
				a = difference;
				return derogateLaw (start, a, b, count, numbers);
			}
			else
			{
				start++;
				int moved = ( a = a > b ? b : a ) << count;
				if (start < numbers.length)
				{
					return derogateLaw (start, moved, numbers[start], 0, numbers);
				}
				else
				{
					return moved;
				}
			}
		}
		return 1;
	}

	private static int[] isLegaled ( int a, int b, int... numbers ) throws Exception
	{
		if (numbers.length < 2)
		{
			throw new Exception ("参数不够 ! ");
		}
		if (a == -1 && b == -1)
		{
			a = numbers[0];
			b = numbers[1];
		}
		String reg = "^\\+?\\d+$";
		if (!String.valueOf (b).matches (reg) || !String.valueOf (a).matches (reg))
		{
			throw new Exception ("相比较的两个数必须是: 非负整数 !");
		}
		return new int[] { a, b };
	}

	private static List<String> formatArray ( String mapstr )
	{
		mapstr = mapstr.substring (0, mapstr.length () - 1);
		String[] temp = mapstr.split ("\\|");
		return Arrays.asList (temp);
	}
}
