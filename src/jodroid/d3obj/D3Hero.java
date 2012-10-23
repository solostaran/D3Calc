package jodroid.d3obj;

/**
 * Represents the complete D3 Hero that the D3api provides in a JSON file.<br/>
 * Resource JSON file can be found with :<br/>
 * <ul>
 * <li>battletag-name ::= &lt;regional battletag allowed characters&gt;</li>
 * <li>battletag-code ::= &lt;integer&gt;</li>
 * <li>hero-id ::= &lt;integer&gt;</li>
 * <li>url ::= &lt;host&gt; "/api/d3/profile/" &lt;battletag-name&gt; "-" &lt;battletag-code&gt; "/hero/" &lt;hero-id&gt;</li>
 * </ul>
 * The "hero-id" can be found in the player profile JSON file.
 * @author JRD
 * @see D3HeroLite
 * @see D3Profile
 * @see <a href="http://blizzard.github.com/d3-api-docs/">Diablo 3 Web API</a>
 */
public class D3Hero extends D3HeroLite {

}
